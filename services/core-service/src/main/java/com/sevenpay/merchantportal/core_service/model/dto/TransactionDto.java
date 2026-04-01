package com.sevenpay.merchantportal.core_service.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDto {
    private String transactionId;
    private String transactionType;
    private BigDecimal amount;
    private String currency;
    private String authorizationState;
    private String validity;
    private String cardBrand;
    private String acquirerHost;
    private String authNumber;
    private LocalDateTime timestamp;
}
