package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDto;
import com.sevenpay.merchantportal.core_service.service.TerminalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terminals")
public class TerminalController {

    private final TerminalService terminalService;

    @GetMapping()
    public ResponseEntity<ApiResponse<TerminalDto>> getTerminals(
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false) String merchantOib,
            @RequestParam(required = false) String mid
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(terminalService.getTerminals(merchantOib, mid, pageable));
    }

    @GetMapping("/{tid}")
    public ResponseEntity<TerminalDetailDto> getTerminalByTid(
            @PathVariable String tid
    ){
        return ResponseEntity.ok(terminalService.getTerminalByTid(tid));
    }

}

