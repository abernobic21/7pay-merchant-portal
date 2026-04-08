package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.dto.common.AcquirerTerminalDataDto;
import com.sevenpay.merchantportal.core_service.model.enums.TerminalStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TerminalDetailDto {
    private String tid;
    private String terminalType;
    private TerminalStatus status;
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

