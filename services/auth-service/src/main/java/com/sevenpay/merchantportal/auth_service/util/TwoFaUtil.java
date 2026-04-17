package com.sevenpay.merchantportal.auth_service.util;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TwoFaUtil {

    private final SecretGenerator secretGenerator;
    private final PasswordEncoder passwordEncoder;
    private final CodeVerifier codeVerifier;

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public String generateQrCodeUrl(String issuer, String email, String secret) {
        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer,
                email,
                secret,
                issuer
        );
    }

    public boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }

    public List<String> generateBackupCodes(int count) {

        SecureRandom random = new SecureRandom();
        List<String> codes = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            codes.add(generateCode(random));
        }

        return codes;
    }

    private String generateCode(SecureRandom random) {

        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    public String hashBackupCode(String code) {
        return passwordEncoder.encode(code);
    }
}
