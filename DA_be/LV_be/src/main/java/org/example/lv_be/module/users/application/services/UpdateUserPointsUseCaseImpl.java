package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.MemberTier; // Import Enum của bạn
import org.example.lv_be.module.users.application.interfaces.IUpdateUserPointsUseCase;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserPointsUseCaseImpl implements IUpdateUserPointsUseCase {

    private final IUserRepository userRepository;

    @Override
    @Transactional
    public void execute(Long userId, int pointsToAdd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        int currentPoints = user.getPoints() != null ? user.getPoints() : 0;
        int newTotalPoints = currentPoints + pointsToAdd;
        user.setPoints(newTotalPoints);

        // 🌟 SỬA TẠI ĐÂY: Dùng Enum thay vì String
        if (newTotalPoints >= 5000) {
            user.setMemberTier(MemberTier.DIAMOND);
        } else if (newTotalPoints >= 2000) {
            user.setMemberTier(MemberTier.GOLD);
        } else if (newTotalPoints >= 500) {
            user.setMemberTier(MemberTier.SILVER);
        } else {
            user.setMemberTier(MemberTier.NEW);
        }

        userRepository.save(user);
    }
}