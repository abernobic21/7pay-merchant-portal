package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDto;
import com.sevenpay.merchantportal.core_service.service.SalepointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/salepoints")
public class SalepointController {

    private final SalepointService salepointService;

    @GetMapping()
    public ResponseEntity<ApiResponse<SalepointDto>> getSalepoint(
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false) String merchantOib) {
        return ResponseEntity.status(HttpStatus.OK).body(salepointService.getSalepoints(merchantOib, pageable));
    }

}

