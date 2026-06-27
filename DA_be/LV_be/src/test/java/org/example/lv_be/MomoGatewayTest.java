package org.example.lv_be;

import org.example.lv_be.module.billing.application.interfaces.out.IMomoGatewayClient;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class MomoGatewayTest {

    @Autowired
    private IMomoGatewayClient momoGatewayClient;

    @Test
    public void testCreateMomoPaymentUrl() {
        System.out.println(">>> Đang giả lập tạo đơn hàng ảo...");
        
        // 1. Tạo một Invoice giả lập để test
        Invoice fakeInvoice = Invoice.builder()
                .id(999L)
                .code("INV-999")
                .totalAmount(new BigDecimal("25000")) // 25.000 VNĐ
                .build();

        System.out.println(">>> Đang gửi Request sang máy chủ MoMo...");
        
        // 2. Gọi hàm tạo URL
        String payUrl = momoGatewayClient.createPaymentUrl(fakeInvoice);

        System.out.println("=========================================");
        System.out.println(">>> KẾT QUẢ TẠO LINK THANH TOÁN MOMO THÀNH CÔNG!");
        System.out.println(">>> CLICK VÀO ĐÂY ĐỂ THANH TOÁN (PAY URL): " + payUrl);
        System.out.println("=========================================");
    }
}
