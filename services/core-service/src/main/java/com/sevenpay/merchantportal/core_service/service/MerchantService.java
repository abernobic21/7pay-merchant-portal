package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantApiClient merchantApiClient;

    public ApiResponse<MerchantDto> getMerchants(){

        ApiResponse<RawMerchantDto> rawResponse = merchantApiClient.fetchMerchants();

        List<MerchantDto> merchants = rawResponse.getContent()
                .stream()
                .map(this::toMerchantDto)
                .collect(Collectors.toList());

        ApiResponse<MerchantDto> response = new ApiResponse<>();
        response.setContent(merchants);
        response.setPage(rawResponse.getPage());
        return response;
    }

    private MerchantDto toMerchantDto(RawMerchantDto rawMerchantDto){
        return MerchantDto.builder()
                .shortName(rawMerchantDto.getShortName())
                .fullName(rawMerchantDto.getFullName())
                .oib(rawMerchantDto.getOib())
                .status(rawMerchantDto.getStatus())
                .address(rawMerchantDto.getAddress())
                .build();
    }
}
