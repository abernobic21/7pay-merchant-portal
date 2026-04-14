package com.sevenpay.merchantportal.auth_service.model.dto;

import com.sevenpay.merchantportal.auth_service.model.enums.AccessLevel;
import com.sevenpay.merchantportal.auth_service.model.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class JwtTokenValidationResponse {
    private UUID userId;
    private Role role;
    private AccessLevel accessLevel;
    private String merchantId;
}
