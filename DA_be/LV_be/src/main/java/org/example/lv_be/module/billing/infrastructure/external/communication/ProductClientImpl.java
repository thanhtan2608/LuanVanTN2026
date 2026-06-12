package org.example.lv_be.module.billing.infrastructure.external.communication;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.application.interfaces.out.IProductClient;
import org.example.lv_be.module.catalog.application.interfaces.product.IUpdateInventoryUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements IProductClient {

     private final IUpdateInventoryUseCase updateInventoryUseCase; //(Inject UseCase của module Product vào đây)

    @Override
    public void deductInventory(Long productId, int quantity) {
        // Thực thi logic trừ kho của module Product tại đây
         updateInventoryUseCase.execute(productId, -quantity);
    }
}