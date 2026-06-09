package org.example.lv_be.module.ailookbook.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.ailookbook.application.dto.LookbookDetailResponse;
import org.example.lv_be.module.ailookbook.application.interfaces.IGetLookbookDetailUseCase;
import org.example.lv_be.module.ailookbook.application.mappers.LookbookMapper;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.domain.repository.ILookbookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLookbookDetailUseCaseImpl implements IGetLookbookDetailUseCase {

    private final ILookbookRepository lookbookRepository;
    private final LookbookMapper lookbookMapper;
    private final JdbcTemplate jdbcTemplate; // Dùng để truy vấn nhanh dữ liệu liên bảng từ module dịch vụ thực tế

    @Override
    @Transactional(readOnly = true)
    public LookbookDetailResponse execute(Long id) {
        LookbookItem item = lookbookRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin chi tiết mẫu tóc AI!"));

        LookbookDetailResponse response = lookbookMapper.toDetailResponse(item);

        // MÓC NỐI KINH DOANH: Quét bảng dịch vụ thực tế có cùng mã hairstyle_id để gợi ý combo bảng giá cho khách đặt lịch
        if (item.getHairstyleId() != null) {
            String sql = "SELECT id, service_name, price, duration_minutes FROM hairstyle_services WHERE hairstyle_id = ? AND is_deleted = 0";
            try {
                List<LookbookDetailResponse.ActualServiceDto> actualServices = jdbcTemplate.query(
                        sql,
                        new BeanPropertyRowMapper<>(LookbookDetailResponse.ActualServiceDto.class),
                        item.getHairstyleId()
                );
                response.setActualServices(actualServices);
            } catch (Exception e) {
                response.setActualServices(List.of()); // Đảm bảo an toàn không sập API nếu module kia chưa hoàn thiện
            }
        }
        return response;
    }
}