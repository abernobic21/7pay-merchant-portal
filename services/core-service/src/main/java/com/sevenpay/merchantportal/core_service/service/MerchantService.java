package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantApiClient merchantApiClient;

    public ApiResponse<MerchantDto> getMerchants(MerchantStatus status, Pageable pageable) {

        new ApiResponse<>();
        ApiResponse<RawMerchantDto> rawResponse;
        if (status != null) {
            rawResponse = merchantApiClient.fetchMerchantsByStatus(status, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        } else {
            rawResponse = merchantApiClient.fetchMerchants(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        }
        List<MerchantDto> merchants = rawResponse.getContent()
                .stream()
                .map(this::toMerchantDto)
                .collect(Collectors.toList());

        ApiResponse<MerchantDto> response = new ApiResponse<>();
        response.setContent(merchants);
        response.setPage(rawResponse.getPage());
        return response;
    }

    public MerchantDetailDto getMerchantByOib(String oib) {
        RawMerchantDetailDto rawMerchant = merchantApiClient.fetchMerchantByOib(oib);

        return toMerchantDetailDto(rawMerchant);
    }

    private MerchantDto toMerchantDto(RawMerchantDto rawMerchantDto) {
        return MerchantDto.builder()
                .shortName(rawMerchantDto.getShortName())
                .fullName(rawMerchantDto.getFullName())
                .oib(rawMerchantDto.getOib())
                .status(MerchantStatus.getByName(rawMerchantDto.getStatus()))
                .address(rawMerchantDto.getAddress())
                .build();
    }

    private MerchantDetailDto toMerchantDetailDto(RawMerchantDetailDto rawMerchantDto) {
        return MerchantDetailDto.builder()
                .shortName(rawMerchantDto.getShortName())
                .fullName(rawMerchantDto.getFullName())
                .oib(rawMerchantDto.getOib())
                .address(rawMerchantDto.getAddress())
                .status(MerchantStatus.getByName(rawMerchantDto.getStatus()))
                .supportedTransactionTypes(rawMerchantDto.getSupportedTransactionTypes())
                .supportedAcquirers(rawMerchantDto.getSupportedAcquirers())
                .supportedCardBrands(rawMerchantDto.getSupportedCardBrands())
                .build();
    }
}
