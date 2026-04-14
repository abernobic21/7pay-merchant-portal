package com.sevenpay.merchantportal.core_service.util;

import com.sevenpay.merchantportal.core_service.client.AuthClient;
import com.sevenpay.merchantportal.core_service.exception.UnauthorizedException;
import com.sevenpay.merchantportal.core_service.model.dto.response.JwtTokenValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthGuard {

    private final AuthClient authClient;

    public JwtTokenValidationResponse authenticate(String accessToken) {
        if (accessToken == null) throw new UnauthorizedException("Unauthorized");
        return authClient.validate(accessToken);
    }
}
