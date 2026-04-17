package com.sevenpay.merchantportal.auth_service.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class TwoFaBackupRequest {
    @NotNull
    private String preAuthToken;
    @NotNull
    private String backupCode;
}
