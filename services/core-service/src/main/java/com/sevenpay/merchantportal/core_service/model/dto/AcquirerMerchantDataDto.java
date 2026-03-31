package com.sevenpay.merchantportal.core_service.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AcquirerMerchantDataDto {
    private String acquirerMid;
    private String acquirerHost;
    private List<String> transactionTypes;
    private List<String> cardBrands;
}
