package com.sevenpay.merchantportal.core_service.client;

import com.sevenpay.merchantportal.core_service.exception.MerchantApiException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.*;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.util.Optional;
import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
public class MerchantApiClient {

    private final RestClient merchantApiRestClient;

    private final Semaphore concurrentRequestSemaphore = new Semaphore(50);

    public ApiResponse<RawMerchantDto> fetchMerchants(int page, int size, Sort sort) {

        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/merchants")
                                .queryParam("page", page)
                                .queryParam("size", size);

                        if (sort != null && sort.isSorted()) {
                            sort.forEach(order -> builder.queryParam(
                                    "sort",
                                    order.getProperty() + "," + order.getDirection().name().toLowerCase()
                            ));
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawMerchantDto> fetchMerchantsByStatus(MerchantStatus status, int page, int size, Sort sort) {

        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/merchants/search")
                                .queryParam("status", status)
                                .queryParam("page", page)
                                .queryParam("size", size);

                        if (sort.isSorted()) {
                            sort.forEach(order -> builder.queryParam(
                                    "sort",
                                    order.getProperty() + "," + order.getDirection().name().toLowerCase()
                            ));
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public RawMerchantDetailDto fetchMerchantByOib(String oib) {
        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/merchants/oib/{oib}/details")
                            .build(oib))
                    .retrieve()
                    .body(RawMerchantDetailDto.class);
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawSalepointDto> fetchSalepoints(int page, int size, Sort sort) {

        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/salepoints")
                                .queryParam("page", page)
                                .queryParam("size", size);

                        if (sort != null && sort.isSorted()) {
                            sort.forEach(order -> builder.queryParam(
                                    "sort",
                                    order.getProperty() + "," + order.getDirection().name().toLowerCase()
                            ));
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawSalepointDto> fetchSalepointsByMerchantOib(String merchantOib, int page, int size, Sort sort) {

        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/salepoints/search")
                                .queryParam("merchantOib", merchantOib)
                                .queryParam("page", page)
                                .queryParam("size", size);

                        if (sort != null && sort.isSorted()) {
                            sort.forEach(order -> builder.queryParam(
                                    "sort",
                                    order.getProperty() + "," + order.getDirection().name().toLowerCase()
                            ));
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public RawSalepointDto fetchSalepointByMid(String mid) {
        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/salepoints/{mid}")
                            .build(mid))
                    .retrieve()
                    .body(RawSalepointDto.class);
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawTerminalDto> fetchTerminals(int page, int size, Sort sort) {
        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/terminals")
                                .queryParam("page", page)
                                .queryParam("size", size);

                        if (sort != null && sort.isSorted()) {
                            sort.forEach(order -> builder.queryParam(
                                    "sort",
                                    order.getProperty() + "," + order.getDirection().name().toLowerCase()
                            ));
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawTerminalDto> fetchTerminalsWithFilters(String merchantOib, String mid, int page, int size, Sort sort) {
        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/terminals/search")
                                .queryParam("page", page)
                                .queryParam("size", size);

                        if(merchantOib != null && !merchantOib.isEmpty())
                            builder.queryParam("merchantOib", merchantOib);

                        if(mid != null && !mid.isEmpty())
                            builder.queryParam("mid", mid);

                        if (sort != null && sort.isSorted()) {
                            sort.forEach(order -> builder.queryParam(
                                    "sort",
                                    order.getProperty() + "," + order.getDirection().name().toLowerCase()
                            ));
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public RawTerminalDto fetchTerminalByTid(String tid) {
        try {
            return merchantApiRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/terminals/{tid}")
                            .build(tid))
                    .retrieve()
                    .body(RawTerminalDto.class);
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public ApiResponse<RawTransactionDto> fetchTransactions(String merchantOib, String timeFrom, String timeUntil, String mid, String tid, int page, int size, Sort sort) {

        try {

            return merchantApiRestClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder
                                .path("/transactions/search")
                                .queryParam("merchantOib", merchantOib)
                                .queryParam("timeFrom", timeFrom)
                                .queryParam("timeUntil", timeUntil)
                                .queryParamIfPresent("mid", Optional.ofNullable(mid))
                                .queryParamIfPresent("tid", Optional.ofNullable(tid))
                                .queryParam("page", page)
                                .queryParam("size", size);

                        if (sort != null && sort.isSorted()) {
                            sort.forEach(order -> builder.queryParam(
                                    "sort",
                                    order.getProperty() + "," + order.getDirection().name().toLowerCase()
                            ));
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }

    public RawTransactionDetailDto fetchTransactionById(String transactionId) {
        try {
            concurrentRequestSemaphore.acquire();
            try {
                return merchantApiRestClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/transactions/{transactionId}/details")
                                .build(transactionId))
                        .retrieve()
                        .body(RawTransactionDetailDto.class);
            } finally {
                concurrentRequestSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MerchantApiException("Interrupted while waiting to send request");
        } catch (RestClientException e) {
            throw new MerchantApiException("External service unavailable");
        }
    }
}
