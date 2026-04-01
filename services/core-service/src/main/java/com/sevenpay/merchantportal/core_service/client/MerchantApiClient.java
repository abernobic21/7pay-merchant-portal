package com.sevenpay.merchantportal.core_service.client;

import com.sevenpay.merchantportal.core_service.exception.MerchantApiException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.*;
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
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawTransactionDto> fetchTransactions(String merchantOib, String timeFrom, String timeUntil) {

        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/transactions/search")
                            .queryParam("merchantOib", merchantOib)
                            .queryParam("timeFrom", timeFrom)
                            .queryParam("timeUntil", timeUntil)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            throw new MerchantApiException("External service unavailable");
        }
    }

    public RawTransactionDetailDto fetchTransactionById(String transactionId) {

        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/transactions/{transactionId}/details")
                            .build(transactionId))
                    .retrieve()
                    .body(RawTransactionDetailDto.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            throw new MerchantApiException("External service unavailable");
        }
    }
}
