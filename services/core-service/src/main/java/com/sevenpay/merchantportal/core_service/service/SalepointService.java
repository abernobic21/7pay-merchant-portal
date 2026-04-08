package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawSalepointDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalepointService {

    private final MerchantApiClient merchantApiClient;

    public ApiResponse<SalepointDto> getSalepoints(String merchantOib, Pageable pageable){

        ApiResponse<RawSalepointDto> rawResponse;

        if (merchantOib != null) {
            rawResponse = merchantApiClient.fetchSalepointsByMerchantOib(merchantOib, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        } else {
            rawResponse = merchantApiClient.fetchSalepoints(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        }

        List<SalepointDto> salepoints = rawResponse.getContent()
                .stream()
                .map(this::toSalepointDto)
                .collect(Collectors.toList());

        ApiResponse<SalepointDto> response = new ApiResponse<>();
        response.setContent(salepoints);
        response.setPage(rawResponse.getPage());
        return response;
    }

    private SalepointDto toSalepointDto(RawSalepointDto rawSalepointDto){
        return SalepointDto.builder()
                .name(rawSalepointDto.getName())
                .mid(rawSalepointDto.getMid())
                .status(rawSalepointDto.getStatus())
                .address(rawSalepointDto.getAddress())
                .merchantOib(rawSalepointDto.getMerchantOib())
                .build();
    }
}
