package com.sevenpay.merchantportal.core_service.controller;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDto;
import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import com.sevenpay.merchantportal.core_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping()
    public ResponseEntity<ApiResponse<TransactionDto>> getTransactions(
            @RequestParam String merchantOib,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeFrom,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeUntil,
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false) String mid,
            @RequestParam(required = false) String tid,
            @RequestParam(required = false) TransactionAuthorizationState status,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) CardBrand cardBrand,
            @RequestParam(required = false) String authNumber,
            @RequestParam(required = false) String rnn
    ) {
        return ResponseEntity.ok(transactionService.getTransactions(merchantOib, timeFrom, timeUntil, mid, tid, status, transactionType, cardBrand, authNumber, rnn, pageable));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDetailDto> getTransactionById(
            @PathVariable String transactionId
    ){
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }
}
