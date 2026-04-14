package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDto;
import com.sevenpay.merchantportal.core_service.service.SalepointService;
import com.sevenpay.merchantportal.core_service.util.AuthGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/salepoints")
public class SalepointController {

    private final SalepointService salepointService;
    private final AuthGuard authGuard;

    @GetMapping()
    public ResponseEntity<ApiResponse<SalepointDto>> getSalepoint(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false) String merchantOib) {

        authGuard.authenticate(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(salepointService.getSalepoints(merchantOib, pageable));
    }

    @GetMapping("/{mid}")
    public ResponseEntity<SalepointDetailDto> getSalepointByMid(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PathVariable String mid){

        authGuard.authenticate(accessToken);
        return ResponseEntity.ok(salepointService.getSalepointByMid(mid));
    }

}

