package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.dto.response.DashboardStatsDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.JwtTokenValidationResponse;
import com.sevenpay.merchantportal.core_service.service.DashboardService;
import com.sevenpay.merchantportal.core_service.util.AuthGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardServiceService;
    private final AuthGuard authGuard;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeFrom,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeUntil,
            @RequestParam(required = false) String mid,
            @RequestParam(required = false) String tid) {

        JwtTokenValidationResponse accessTokenData = authGuard.authenticate(accessToken);
        return ResponseEntity.ok(dashboardServiceService.getStats(accessTokenData.getMerchantId(), timeFrom, timeUntil, mid, tid));
    }
}
