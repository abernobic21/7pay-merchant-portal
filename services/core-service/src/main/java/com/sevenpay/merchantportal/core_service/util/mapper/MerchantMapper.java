package com.sevenpay.merchantportal.core_service.util.mapper;

import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;

public class MerchantMapper {
    private MerchantMapper(){}

    public static MerchantDto toMerchantDto(RawMerchantDto rawMerchantDto) {
        return MerchantDto.builder()
                .shortName(rawMerchantDto.getShortName())
                .fullName(rawMerchantDto.getFullName())
                .oib(rawMerchantDto.getOib())
                .status(MerchantStatus.getByName(rawMerchantDto.getStatus()))
                .address(rawMerchantDto.getAddress())
                .build();
    }

    public static MerchantDetailDto toMerchantDetailDto(RawMerchantDetailDto rawMerchantDto) {
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
