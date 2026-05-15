package com.sevenpay.merchantportal.report_service.model.dto.response;

import com.sevenpay.merchantportal.report_service.model.enums.Role;
import com.sevenpay.merchantportal.report_service.model.enums.AccessLevel;
import lombok.*;

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

