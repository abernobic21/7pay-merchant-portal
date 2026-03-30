package com.sevenpay.merchantportal.core_service.client;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.RawMerchantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class MerchantClient {

    private final RestClient merchantApiClient;

    public ApiResponse<RawMerchantDto> fetchMerchants() {
        return merchantApiClient.get()
                .uri("/merchants")
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<RawMerchantDto>>() {});
    }
}
