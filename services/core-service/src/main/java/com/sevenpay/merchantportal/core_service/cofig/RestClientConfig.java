package com.sevenpay.merchantportal.core_service.cofig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${merchant.api.base-url}")
    private String merchantApiBaseUrl;

    @Value("${merchant.api.key}")
    private String merchantApiKey;

    @Bean
    public RestClient merchantApiClient() {
        return RestClient.builder()
                .baseUrl(merchantApiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("X-API-KEY", merchantApiKey)
                .build();
    }
}
