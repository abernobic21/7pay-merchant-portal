package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDto;
import com.sevenpay.merchantportal.core_service.service.TerminalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terminals")
public class TerminalController {

    private final TerminalService terminalService;

    @GetMapping()
    public ResponseEntity<ApiResponse<TerminalDto>> getTerminals() {
        return ResponseEntity.status(HttpStatus.OK).body(terminalService.getTerminals());
    }

}

