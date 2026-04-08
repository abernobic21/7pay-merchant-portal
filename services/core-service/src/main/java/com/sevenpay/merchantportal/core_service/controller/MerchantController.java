package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import com.sevenpay.merchantportal.core_service.service.MerchantService;
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
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<MerchantDto>> getMerchants(
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false)MerchantStatus status
            ){
        return ResponseEntity.status(HttpStatus.OK).body(merchantService.getMerchants(status, pageable));
    }

}

