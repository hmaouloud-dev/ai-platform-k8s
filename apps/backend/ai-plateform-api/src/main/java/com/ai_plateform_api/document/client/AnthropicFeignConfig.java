package com.ai_plateform_api.document.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class AnthropicFeignConfig {

    @Bean
    public RequestInterceptor anthropicInterceptor(
            @Value("${anthropic.api.key}") String apiKey
    ) {
        return template -> {
            template.header("x-api-key", apiKey);
            template.header("anthropic-version", "2023-06-01");
            template.header("content-type", "application/json");
        };
    }
}