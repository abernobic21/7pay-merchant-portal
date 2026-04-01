package com.sevenpay.merchantportal.core_service.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TerminalDto {
    private String tid;
    private String terminalType;
    private String status;
    private String midReference;
    private String merchantCategoryCode;
    private String currency;
}
