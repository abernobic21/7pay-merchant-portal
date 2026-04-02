package com.sevenpay.merchantportal.core_service.model.dto.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RawTransactionDto {
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
}
