package com.sevenpay.merchantportal.core_service.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RawTerminalDto {
    private String tid;
    private String terminalType;
    private String status;
    private String midReference;
    private String phoneNumber;
    private String emailContact;
    private String currency;
    private String merchantCategoryCode;
    private String authenticationMode;
    private String softPosId;
    private String softPosStatus;
    private Boolean tipEnabled;
    private List<AcquirerTerminalDataDto> acquirerTerminalData;
}

