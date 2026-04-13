package com.sevenpay.merchantportal.user_management_service.util;

import com.sevenpay.merchantportal.user_management_service.client.AuthClient;
import com.sevenpay.merchantportal.user_management_service.exception.ForbiddenException;
import com.sevenpay.merchantportal.user_management_service.exception.UnauthorizedException;
import com.sevenpay.merchantportal.user_management_service.model.dto.JwtTokenValidationResponse;
import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthGuard {

    private final AuthClient authClient;

    public JwtTokenValidationResponse authenticate(String accessToken) {
        if (accessToken == null) throw new UnauthorizedException("Missing token");
        return authClient.validate(accessToken);
    }

    public void requireAdmin(String accessToken) {
        JwtTokenValidationResponse tokenData = authenticate(accessToken);
        if (tokenData.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Admin access required");
        }
    }
}
