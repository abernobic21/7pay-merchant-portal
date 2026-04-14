package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import com.sevenpay.merchantportal.core_service.service.MerchantService;
import com.sevenpay.merchantportal.core_service.util.AuthGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;
    private final AuthGuard authGuard;

    @GetMapping("")
    public ResponseEntity<ApiResponse<MerchantDto>> getMerchants(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false)MerchantStatus status){

        authGuard.authenticate(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(merchantService.getMerchants(status, pageable));
    }

    @GetMapping("/{oib}")
    public ResponseEntity<MerchantDetailDto> getMerchantByOib(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PathVariable String oib){

        authGuard.authenticate(accessToken);
        return ResponseEntity.ok(merchantService.getMerchantByOib(oib));
    }


}

