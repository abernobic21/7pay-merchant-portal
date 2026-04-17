package com.sevenpay.merchantportal.auth_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwoFaSetupResponse {
    private String secret;
    private String qrCodeUrl;
    private List<String> backupCodes;
}
