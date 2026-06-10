package org.example.lv_be.module.hairstyles.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.hairstyles.application.interfaces.ISyncHairstyleServicesUseCase;
import org.example.lv_be.module.hairstyles.domain.entity.HairstyleService;
import org.example.lv_be.module.hairstyles.domain.exception.HairstyleDomainException;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SyncHairstyleServicesUseCaseImpl implements ISyncHairstyleServicesUseCase {

    private final IHairstyleRepository hairstyleRepository;
    private final IHairstyleServiceRepository hairstyleServiceRepository;

    @Override
    @Transactional
    public void execute(Long hairstyleId, List<Long> serviceIds) {
        // Kiểm tra xem kiểu tóc cha có tồn tại sạch trong DB không
        hairstyleRepository.findById(hairstyleId)
                .orElseThrow(() -> new HairstyleDomainException("Không thể đồng bộ dữ liệu dịch vụ cho kiểu tóc không tồn tại!"));

        // Bước 1: Dọn sạch toàn bộ các bản ghi liên kết cũ của kiểu tóc này trong bảng trung gian
        hairstyleServiceRepository.deleteAllLinksByHairstyleId(hairstyleId);

        // Bước 2: Nạp loạt danh sách ID dịch vụ mới do Admin chỉ định vào bảng trung gian
        if (serviceIds != null && !serviceIds.isEmpty()) {
            for (Long sId : serviceIds) {
                HairstyleService link = HairstyleService.builder()
                        .hairstyleId(hairstyleId)
                        .serviceId(sId)
                        .build();
                link.validateLink();
                hairstyleServiceRepository.saveLink(link);
            }
        }
    }
}