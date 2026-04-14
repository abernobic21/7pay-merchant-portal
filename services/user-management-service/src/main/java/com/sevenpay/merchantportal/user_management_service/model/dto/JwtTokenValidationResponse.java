package com.sevenpay.merchantportal.user_management_service.model.dto;

import com.sevenpay.merchantportal.user_management_service.model.enums.AccessLevel;
import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenValidationResponse {
    private UUID userId;
    private Role role;
    private AccessLevel accessLevel;
    private String merchantId;
}
