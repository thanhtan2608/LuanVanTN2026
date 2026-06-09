package org.example.lv_be.module.ailookbook.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiLookbookConfig {

    /**
     * Khởi tạo RestTemplate để thực hiện các cuộc gọi API HTTP sang Server AI bên thứ ba
     */
    @Bean
    public RestTemplate aiRestTemplate() {
        return new RestTemplate();
    }
}