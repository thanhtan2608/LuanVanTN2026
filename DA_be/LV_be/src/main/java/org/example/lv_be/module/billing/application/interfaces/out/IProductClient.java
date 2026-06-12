package org.example.lv_be.module.billing.application.interfaces.out;
public interface IProductClient {
    void deductInventory(Long productId, int quantity);
}
