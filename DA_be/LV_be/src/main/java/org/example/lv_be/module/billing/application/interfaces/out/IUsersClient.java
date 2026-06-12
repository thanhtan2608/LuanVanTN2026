package org.example.lv_be.module.billing.application.interfaces.out;

import java.math.BigDecimal;

public interface IUsersClient {
    void addRewardPoints(Long customerId, BigDecimal finalAmount);
}
