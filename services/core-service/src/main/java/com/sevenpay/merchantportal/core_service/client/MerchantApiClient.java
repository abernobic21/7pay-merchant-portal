package com.sevenpay.merchantportal.core_service.client;

import com.sevenpay.merchantportal.core_service.exception.MerchantApiException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.RawMerchantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class MerchantApiClient {

    private final RestClient merchantApiRestClient;

    public ApiResponse<RawMerchantDto> fetchMerchants() {

        try {
            return merchantApiRestClient.get()
                    .uri("/merchants")
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<RawMerchantDto>>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }
}
