package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.RawSalepointDto;
import com.sevenpay.merchantportal.core_service.model.dto.SalepointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalepointService {

    private final MerchantApiClient merchantApiClient;

    public ApiResponse<SalepointDto> getSalepoints(){

        ApiResponse<RawSalepointDto> rawResponse = merchantApiClient.fetchSalepoints();

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
