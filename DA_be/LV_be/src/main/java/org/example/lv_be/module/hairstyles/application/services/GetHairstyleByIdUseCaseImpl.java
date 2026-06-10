package org.example.lv_be.module.hairstyles.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleDetailResponse;
import org.example.lv_be.module.hairstyles.application.interfaces.IGetHairstyleByIdUseCase;
import org.example.lv_be.module.hairstyles.application.mapper.HairstyleMapper;
import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import org.example.lv_be.module.hairstyles.domain.exception.HairstyleDomainException;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetHairstyleByIdUseCaseImpl implements IGetHairstyleByIdUseCase {

    private final IHairstyleRepository hairstyleRepository;
    private final IHairstyleServiceRepository hairstyleServiceRepository;
    private final HairstyleMapper hairstyleMapper;

    // 🌟 GỌI LIÊN MODULE: Tiêm cổng Repository của module Catalog để bốc thông tin gói dịch vụ thật
    private final IServiceRepository catalogServiceRepository;

    @Override
    public HairstyleDetailResponse execute(Long id) {
        Hairstyle hairstyle = hairstyleRepository.findById(id)
                .orElseThrow(() -> new HairstyleDomainException("Kiểu tóc yêu cầu xem không tồn tại!"));

        // Khởi tạo DTO phản hồi chi tiết
        HairstyleDetailResponse response = hairstyleMapper.toDetailResponse(hairstyle);

        // Bốc danh sách mã service_id từ bảng trung gian Many-to-Many
        List<Long> linkedServiceIds = hairstyleServiceRepository.findServiceIdsByHairstyleId(id);
        List<HairstyleDetailResponse.AssociatedServiceInfo> servicesList = new ArrayList<>();

        // Quét từng ID dịch vụ, gọi sang lõi Catalog để nạp đầy đủ Tên, Giá, Số phút thực hiện thật
        if (linkedServiceIds != null && !linkedServiceIds.isEmpty()) {
            for (Long sId : linkedServiceIds) {
                catalogServiceRepository.findById(sId).ifPresent(catalogItem -> {
                    HairstyleDetailResponse.AssociatedServiceInfo info = HairstyleDetailResponse.AssociatedServiceInfo.builder()
                            .serviceId(catalogItem.getId())
                            .serviceName(catalogItem.getName())
                            .price(catalogItem.getPrice())
                            .durationMinutes(catalogItem.getDurationMinutes())
                            .build();
                    servicesList.add(info);
                });
            }
        }

        response.setActualServices(servicesList);
        return response;
    }
}