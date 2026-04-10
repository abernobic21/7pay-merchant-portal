package com.sevenpay.merchantportal.core_service.util.mapper;

import com.sevenpay.merchantportal.core_service.model.dto.external.RawSalepointDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDto;
import com.sevenpay.merchantportal.core_service.model.enums.SalepointStatus;

public class SalepointMapper {
    private SalepointMapper() {
    }

    ;

    public static SalepointDto toSalepointDto(RawSalepointDto rawSalepointDto) {
        return SalepointDto.builder()
                .name(rawSalepointDto.getName())
                .mid(rawSalepointDto.getMid())
                .status(rawSalepointDto.getStatus())
                .address(rawSalepointDto.getAddress())
                .merchantOib(rawSalepointDto.getMerchantOib())
                .build();
    }

    public static SalepointDetailDto toSalepointDetailDto(RawSalepointDto rawSalepointDto) {
        return SalepointDetailDto.builder()
                .name(rawSalepointDto.getName())
                .mid(rawSalepointDto.getMid())
                .status(SalepointStatus.getByName(rawSalepointDto.getStatus()))
                .address(rawSalepointDto.getAddress())
                .merchantOib(rawSalepointDto.getMerchantOib())
                .acquirerMerchantData(rawSalepointDto.getAcquirerMerchantData())
                .build();
    }
}
