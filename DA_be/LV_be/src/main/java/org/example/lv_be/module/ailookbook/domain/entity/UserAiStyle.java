package org.example.lv_be.module.ailookbook.domain.entity;

import lombok.*;
import org.example.lv_be.module.ailookbook.domain.enums.AiProcessStatus;
import org.example.lv_be.module.ailookbook.domain.exception.InvalidAiOperationException;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAiStyle {
    private Long id;
    private Long userId;
    private Long lookbookItemId;
    private String sourceImageUrl; // Ảnh chân dung mặt thật do khách upload lên mây
    private String resultImageUrl; // Ảnh thành phẩm cuối cùng sau khi AI ghép tóc mới
    private AiProcessStatus status;
    private String errorMessage;   // Nhật ký lưu vết lỗi chi tiết nếu hệ thống AI sập/từ chối
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // ==========================================
    // Logic Nghiệp Vụ Điều Khiển Vòng Đời AI
    // ==========================================

    /**
     * Kích hoạt tiến trình: Đánh dấu tác vụ đã thoát khỏi hàng đợi và bắt đầu xử lý vẽ ảnh
     */
    public void startProcessing() {
        if (this.status != AiProcessStatus.PENDING) {
            throw new InvalidAiOperationException("Chỉ có thể chạy xử lý đối với tác vụ đang ở trạng thái chờ (PENDING)!");
        }
        this.status = AiProcessStatus.PROCESSING;
    }

    /**
     * Bàn giao kết quả: Khi Engine AI bên thứ ba xử lý thành công và trả về link ảnh thành phẩm
     */
    public void completeProcessing(String resultUrl) {
        if (this.status != AiProcessStatus.PROCESSING) {
            throw new InvalidAiOperationException("Không thể bàn giao kết quả cho một tác vụ chưa từng được xử lý!");
        }
        if (resultUrl == null || resultUrl.isBlank()) {
            throw new InvalidAiOperationException("Đường dẫn ảnh thành phẩm trả về từ máy chủ AI không hợp lệ!");
        }
        this.resultImageUrl = resultUrl;
        this.status = AiProcessStatus.SUCCESS;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Đóng vết lỗi: Ghi nhận sự cố nếu luồng gọi API AI bị sập, ngắt kết nối hoặc lỗi ảnh đầu vào
     */
    public void failProcessing(String reason) {
        if (this.status == AiProcessStatus.SUCCESS) {
            throw new InvalidAiOperationException("Tác vụ này đã xử lý thành công trước đó, không thể đánh dấu thất bại!");
        }
        this.status = AiProcessStatus.FAILED;
        this.errorMessage = (reason != null && !reason.isBlank()) ? reason : "Lỗi hệ thống bất khả kháng từ máy chủ AI.";
        this.completedAt = LocalDateTime.now();
    }
}