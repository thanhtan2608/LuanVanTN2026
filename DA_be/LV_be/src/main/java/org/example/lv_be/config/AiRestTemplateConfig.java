package org.example.lv_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiRestTemplateConfig {
    @Bean
    public RestTemplate aiRestTemplate() {
        return new RestTemplate();
    }
}