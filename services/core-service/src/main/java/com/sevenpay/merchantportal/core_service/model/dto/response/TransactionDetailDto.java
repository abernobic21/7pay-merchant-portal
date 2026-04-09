package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailDto {
    private String transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String currency;
    private TransactionValidity validity;
    private TransactionAuthorizationState authorizationState;
    private String responseCode;
    private String authNumber;
    private String acquirerHost;
    private String acquirerTid;
    private String acquirerMid;
    private LocalDateTime timestamp;
    private String panMasked;
    private String panExpireDate;
    private CardBrand cardBrand;
    private BigDecimal amount2;
    private String currency2;
    private String stan;
    private String acquirerStan;
    private String rrn;
    private String acquirerRrn;
    private String tid;
    private String mid;
    private String paymentType;
    private Integer installmentNumber;
    private TransactionStateMDCProcessing transactionStateMDCProcessing;
    private LocalDateTime posTimestamp;
    private LocalDateTime acquirerTimestamp;
    private String transactionReference;
    private BigDecimal tipAmount;
}
