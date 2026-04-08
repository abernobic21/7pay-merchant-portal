package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.dto.common.AddressDto;
import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MerchantDetailDto {
    private String shortName;
    private String fullName;
    private String oib;
    private AddressDto address;
    private MerchantStatus status;
    private List<TransactionType> supportedTransactionTypes;
    private List<String> supportedAcquirers;
    private List<CardBrand> supportedCardBrands;
}
