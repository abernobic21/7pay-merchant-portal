package com.sevenpay.merchantportal.report_service.client;


import com.sevenpay.merchantportal.report_service.exception.UnauthorizedException;
import com.sevenpay.merchantportal.report_service.model.dto.response.JwtTokenValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {
    private final RestClient restClient;

    public AuthClient(@Value("${auth.service.url}") String authServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }

    public JwtTokenValidationResponse validate(String accessToken) {
        try {
            return restClient.get()
                    .uri("/api/v1/auth/validate")
                    .cookie("access_token", accessToken)
                    .retrieve()
                    .body(JwtTokenValidationResponse.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}
