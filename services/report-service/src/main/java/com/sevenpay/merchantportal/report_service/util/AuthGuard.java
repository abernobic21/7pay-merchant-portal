package com.sevenpay.merchantportal.report_service.util;

import com.sevenpay.merchantportal.report_service.client.AuthClient;
import com.sevenpay.merchantportal.report_service.exception.UnauthorizedException;
import com.sevenpay.merchantportal.report_service.model.dto.response.JwtTokenValidationResponse;
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
