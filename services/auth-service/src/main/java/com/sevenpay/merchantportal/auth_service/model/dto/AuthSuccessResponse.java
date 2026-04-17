package com.sevenpay.merchantportal.auth_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSuccessResponse {
    private long expiresIn;
    private boolean firstLogin;

    private boolean requires2fa;
    private String preAuthToken;
}
