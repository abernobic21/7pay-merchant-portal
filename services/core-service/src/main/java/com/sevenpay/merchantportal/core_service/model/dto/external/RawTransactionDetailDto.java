package com.sevenpay.merchantportal.core_service.model.dto.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RawTransactionDetailDto {
    private String transactionId;
    private String transactionType;
    private BigDecimal amount;
    private String currency;
    private String validity;
    private String authorizationState;
    private String responseCode;
    private String authNumber;
    private String acquirerHost;
    private String acquirerTid;
    private String acquirerMid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    private String panMasked;
    private String panExpireDate;
    private String cardBrand;
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
    private String transactionStateMDCProcessing;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime posTimestamp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime acquirerTimestamp;
    private String transactionReference;
    private BigDecimal tipAmount;
}
