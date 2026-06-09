package org.example.lv_be.module.ailookbook.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.ailookbook.application.dto.AiStyleResponse;
import org.example.lv_be.module.ailookbook.application.dto.AiTryOnRequest;
import org.example.lv_be.module.ailookbook.application.interfaces.IRequestAiTryOnUseCase;
import org.example.lv_be.module.ailookbook.application.interfaces.external.IAiEngineService;
import org.example.lv_be.module.ailookbook.application.interfaces.external.ICloudStorageService;
import org.example.lv_be.module.ailookbook.application.mappers.UserAiStyleMapper;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.domain.entity.UserAiStyle;
import org.example.lv_be.module.ailookbook.domain.enums.AiProcessStatus;
import org.example.lv_be.module.ailookbook.domain.repository.ILookbookRepository;
import org.example.lv_be.module.ailookbook.domain.repository.IUserAiStyleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestAiTryOnUseCaseImpl implements IRequestAiTryOnUseCase {

    private final ILookbookRepository lookbookRepository;
    private final IUserAiStyleRepository userAiStyleRepository;
    private final ICloudStorageService cloudStorageService;
    private final IAiEngineService aiEngineService;
    private final UserAiStyleMapper aiStyleMapper;

    @Override
    @Transactional
    public AiStyleResponse execute(Long userId, AiTryOnRequest request) { // 🌟 Đổi thành 2 tham số ở đây

        // 1. Kiểm tra mẫu tóc AI khách chọn thông qua request
        LookbookItem lookbookItem = lookbookRepository.findById(request.getLookbookItemId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Mẫu tóc AI bạn chọn thử không tồn tại trên hệ thống!"));

        // 2. LÔI FILE ẢNH TRỰC TIẾP TỪ REQUEST ĐỂ UPLOAD LÊN IMAGEKIT
        if (request.getCustomerImage() == null || request.getCustomerImage().isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Hình ảnh khuôn mặt chân dung để ghép không được để trống!");
        }
        String sourceUrl = cloudStorageService.uploadFile(request.getCustomerImage(), "customer-tryons");

        // 3. Khởi tạo Task dữ liệu PENDING
        UserAiStyle aiStyleTask = UserAiStyle.builder()
                .userId(userId)
                .lookbookItemId(request.getLookbookItemId())
                .sourceImageUrl(sourceUrl)
                .status(AiProcessStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        UserAiStyle savedTask = userAiStyleRepository.save(aiStyleTask);

        // 4. Kích hoạt tiến trình chạy ngầm Google AI bất đồng bộ
        CompletableFuture.runAsync(() -> processAiGenerationInBackground(savedTask.getId(), lookbookItem.getPrompt()));

        // 5. Trả response về cho Controller
        return aiStyleMapper.toResponse(savedTask);
    }

    /**
     * Hàm chạy ngầm biệt lập trong một luồng Thread riêng để tương tác với Server AI nước ngoài
     */
    private void processAiGenerationInBackground(Long taskId, String aiPrompt) {
        log.info("Bắt đầu kích hoạt tiến trình xử lý ngầm ảnh AI cho Task ID: {}", taskId);

        // Mở một transaction riêng biệt cho Thread ngầm này để cập nhật trạng thái
        try {
            UserAiStyle task = userAiStyleRepository.findById(taskId).orElse(null);
            if (task == null) return;

            // Chuyển sang trạng thái ĐANG XỬ LÝ (PROCESSING)
            task.startProcessing();
            userAiStyleRepository.save(task);

            // Gọi API Engine AI xử lý ảnh (Tốn từ 10s - 30s tùy server tải)
            String aiResultUrl = aiEngineService.generateHairStyle(task.getSourceImageUrl(), aiPrompt);

            // Đồng bộ dữ liệu thành công
            task.completeProcessing(aiResultUrl);
            userAiStyleRepository.save(task);
            log.info("Xử lý ảnh AI thành công tốt đẹp cho Task ID: {}", taskId);

        } catch (Exception e) {
            log.error("Hệ thống Engine AI sập hoặc từ chối xử lý cho Task ID: {}. Lý do: {}", taskId, e.getMessage());
            // Cập nhật trạng thái Thất bại (FAILED) vào DB để báo cáo cho khách
            try {
                UserAiStyle failedTask = userAiStyleRepository.findById(taskId).orElse(null);
                if (failedTask != null) {
                    failedTask.failProcessing(e.getMessage());
                    userAiStyleRepository.save(failedTask);
                }
            } catch (Exception dbEx) {
                log.error("Không thể cập nhật trạng thái lỗi vào DB cho Task ID: {}", taskId, dbEx);
            }
        }
    }
}