package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.SalepointDto;
import com.sevenpay.merchantportal.core_service.service.SalepointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/salepoints")
public class SalepointController {

    private final SalepointService salepointService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<SalepointDto>> getSalepoint(){
        return ResponseEntity.status(HttpStatus.OK).body(salepointService.getSalepoints());
    }

}

