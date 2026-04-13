package com.sevenpay.merchantportal.user_management_service.service;

import com.sevenpay.merchantportal.user_management_service.model.entity.PasswordReset;
import com.sevenpay.merchantportal.user_management_service.model.entity.User;
import com.sevenpay.merchantportal.user_management_service.repository.PasswordResetRepository;
import com.sevenpay.merchantportal.user_management_service.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;

    public String createSetPasswordToken(User user) {
        String rawToken = TokenUtil.generateSecureToken();
        String hashedToken = TokenUtil.hashToken(rawToken);

        PasswordReset reset = PasswordReset.builder()
                .user(user)
                .resetToken(hashedToken)
                .expiry(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetRepository.save(reset);
        return rawToken;
    }
}
