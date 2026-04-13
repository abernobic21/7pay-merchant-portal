package com.sevenpay.merchantportal.auth_service.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public Cookie createAccessTokenCookie(String token, int expiry) {
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
    //    cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        return cookie;
    }

    public Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);
    //    cookie.setSecure(true);
        cookie.setPath("/api/v1/auth/refresh");
        cookie.setMaxAge(28800);
        return cookie;
    }

    public Cookie clearCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
