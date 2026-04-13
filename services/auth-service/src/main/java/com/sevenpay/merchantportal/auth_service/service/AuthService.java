package com.sevenpay.merchantportal.auth_service.service;

import com.sevenpay.merchantportal.auth_service.exception.ForbiddenException;
import com.sevenpay.merchantportal.auth_service.exception.InvalidTokenException;
import com.sevenpay.merchantportal.auth_service.model.dto.SetPasswordRequest;
import com.sevenpay.merchantportal.auth_service.model.entity.PasswordReset;
import com.sevenpay.merchantportal.auth_service.model.entity.User;
import com.sevenpay.merchantportal.auth_service.repository.PasswordResetRepository;
import com.sevenpay.merchantportal.auth_service.repository.UserRepository;
import com.sevenpay.merchantportal.auth_service.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;

    public void setPassword(SetPasswordRequest request) {

        String hashedIncoming = TokenUtil.hashToken(request.getToken());

        PasswordReset record = passwordResetRepository.findByResetToken(hashedIncoming)
                .orElseThrow(() -> new InvalidTokenException());

        if (record.getExpiry().isBefore(LocalDateTime.now()) || record.getUsedAt() != null) {
            throw new InvalidTokenException();
        }

        User user = record.getUser();

        if(!user.getFirstLoginFlag()) {
            throw new ForbiddenException("User not in first login state");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLoginFlag(false);
        userRepository.save(user);

        record.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(record);
    }
}
