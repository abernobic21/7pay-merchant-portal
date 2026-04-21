package com.sevenpay.merchantportal.report_service.model.common;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionReportRow {
    private String merchantOib;
    private String merchantName;
    private String mid;
    private String salepointName;
    private String address;
    private String tid;
    private String transactionId;
    private LocalDateTime timestamp;
    private String transactionType;
    private BigDecimal amount;
    private String currency;
    private String authorizationState;
    private String cardBrand;
    private String panMasked;
    private String authNumber;
    private String rrn;
    private String responseCode;
}
