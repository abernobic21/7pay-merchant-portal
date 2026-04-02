package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionValidity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDto {
    private String transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String currency;
    private TransactionAuthorizationState authorizationState;
    private TransactionValidity validity;
    private CardBrand cardBrand;
    private String acquirerHost;
    private String authNumber;
    private LocalDateTime timestamp;
}
