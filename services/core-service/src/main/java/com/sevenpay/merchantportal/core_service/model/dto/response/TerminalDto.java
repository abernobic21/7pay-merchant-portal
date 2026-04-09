package com.sevenpay.merchantportal.core_service.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalDto {
    private String tid;
    private String terminalType;
    private String status;
    private String midReference;
    private String merchantCategoryCode;
    private String currency;
}
