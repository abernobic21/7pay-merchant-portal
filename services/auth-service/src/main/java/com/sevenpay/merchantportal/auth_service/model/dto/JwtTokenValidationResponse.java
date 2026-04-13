package com.sevenpay.merchantportal.auth_service.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenValidationResponse {
    private String userId;
    private String role;
    private String accessLevel;
    private String merchantId;
}
