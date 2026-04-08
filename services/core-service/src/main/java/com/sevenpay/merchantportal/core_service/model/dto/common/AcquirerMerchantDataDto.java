package com.sevenpay.merchantportal.core_service.model.dto.common;

import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import lombok.Data;

import java.util.List;

@Data
public class AcquirerMerchantDataDto {
    private String acquirerMid;
    private String acquirerHost;
    private List<TransactionType> transactionTypes;
    private List<CardBrand> cardBrands;
}
