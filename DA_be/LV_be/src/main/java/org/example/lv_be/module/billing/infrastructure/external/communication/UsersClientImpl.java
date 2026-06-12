package org.example.lv_be.module.billing.infrastructure.external.communication;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.application.interfaces.out.IUsersClient;
// Import UseCase của module Users
import org.example.lv_be.module.users.application.interfaces.IUpdateUserPointsUseCase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UsersClientImpl implements IUsersClient {

    private final IUpdateUserPointsUseCase updateUserPointsUseCase;

    @Override
    public void addRewardPoints(Long customerId, BigDecimal finalAmount) {
        // Ví dụ: 100,000 VNĐ = 10 điểm
        int pointsToAdd = finalAmount.divide(new BigDecimal("10000")).intValue();
        updateUserPointsUseCase.execute(customerId, pointsToAdd);
    }
}