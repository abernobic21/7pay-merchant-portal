package com.sevenpay.merchantportal.core_service.client;

import com.sevenpay.merchantportal.core_service.exception.MerchantApiException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.RawMerchantDto;
import com.sevenpay.merchantportal.core_service.model.dto.RawSalepointDto;
import com.sevenpay.merchantportal.core_service.model.dto.RawTerminalDto;
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
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawSalepointDto> fetchSalepoints() {

        try {
            return merchantApiRestClient.get()
                    .uri("/salepoints")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawTerminalDto> fetchTerminals() {

        try {
            return merchantApiRestClient.get()
                    .uri("/terminals")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            throw new MerchantApiException("External service unavailable");
        }
    }
}
