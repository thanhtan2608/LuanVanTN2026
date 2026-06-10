package org.example.lv_be.module.hairstyles.domain.entity;

import lombok.*;
import org.example.lv_be.module.hairstyles.domain.exception.HairstyleDomainException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HairstyleService {
    private Long hairstyleId; // bigint(20) -> Khóa chính liên hợp & Khóa ngoại liên kết bảng hairstyles
    private Long serviceId;   // bigint(20) -> Khóa chính liên hợp & Khóa ngoại liên kết bảng services

    /**
     * Xác thực tính hợp lệ của cặp liên kết Nhiều - Nhiều
     */
    public void validateLink() {
        if (this.hairstyleId == null || this.hairstyleId <= 0) {
            throw new HairstyleDomainException("Mã định danh kiểu tóc (Hairstyle ID) liên kết không hợp lệ!");
        }
        if (this.serviceId == null || this.serviceId <= 0) {
            throw new HairstyleDomainException("Mã định danh dịch vụ (Service ID) liên kết không hợp lệ!");
        }
    }
}