package org.example.lv_be.module.users.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.example.lv_be.common.enums.MemberTier;
import org.example.lv_be.module.users.domain.exception.UserDomainException;

@Data
@Builder
public class Customer {
    private Long userId; // Liên kết 1-1 với ID của User
    private Integer points;
    private MemberTier memberTier;

    // Thuộc tính tùy chọn nếu bạn muốn load sẵn thông tin User
    private User user;
    // ==========================================
    // LOGIC NGHIỆP VỤ CỐT LÕI NẰM Ở ĐÂY
    // ==========================================

    /**
     * Logic cộng điểm thưởng và tự động xét thăng hạng
     */
    public void addRewardPoints(int pointsToAdd) {
        if (pointsToAdd <= 0) {
            throw new UserDomainException("Số điểm cộng thêm phải lớn hơn 0");
        }
        this.points += pointsToAdd;
        this.recalculateTier(); // Tự động check lên hạng
    }

    /**
     * Quy tắc xét hạng thành viên (Business Rule)
     */
    private void recalculateTier() {
        if (this.points >= 5000) {
            this.memberTier = MemberTier.DIAMOND;
        } else if (this.points >= 2000) {
            this.memberTier = MemberTier.GOLD;
        } else if (this.points >= 500) {
            this.memberTier = MemberTier.SILVER;
        } else {
            this.memberTier = MemberTier.NEW;
        }
    }
}