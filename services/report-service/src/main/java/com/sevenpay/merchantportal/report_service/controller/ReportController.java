package com.sevenpay.merchantportal.report_service.controller;

import com.sevenpay.merchantportal.report_service.model.common.ReportResult;
import com.sevenpay.merchantportal.report_service.model.dto.request.ReportRequest;
import com.sevenpay.merchantportal.report_service.model.dto.response.JwtTokenValidationResponse;
import com.sevenpay.merchantportal.report_service.service.ReportService;
import com.sevenpay.merchantportal.report_service.util.AuthGuard;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;
    private final AuthGuard authGuard;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generate(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @Valid @RequestBody ReportRequest request) {

        JwtTokenValidationResponse accessTokenData = authGuard.authenticate(accessToken);

        ReportResult result = reportService.generateReport(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, result.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(result.getContent().length))
                .body(result.getContent());
    }
}
