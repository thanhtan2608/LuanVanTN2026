package org.example.lv_be.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hair Salon API Specification")
                        .version("1.0.0")
                        .description("Tài liệu API cho Hệ thống Quản lý và Đặt lịch Tiệm Cắt Tóc")
                        .contact(new Contact()
                                .name("Dev Team")
                                .email("contact@hairsalon.com")));
    }
}