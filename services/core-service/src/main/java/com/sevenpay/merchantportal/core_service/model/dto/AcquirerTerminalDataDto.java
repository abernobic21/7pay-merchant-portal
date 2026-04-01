package com.sevenpay.merchantportal.core_service.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AcquirerTerminalDataDto {
    private String acquirerTid;
    private String acquirerHost;
    private List<String> transactionTypes;
    private List<String> cardBrands;
}

