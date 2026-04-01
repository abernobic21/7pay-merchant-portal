package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.exception.ValidationException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.RawTransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.RawTransactionDto;
import com.sevenpay.merchantportal.core_service.model.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final MerchantApiClient merchantApiClient;

    public ApiResponse<TransactionDto> getTransactions(String merchantOib, LocalDateTime  timeFrom, LocalDateTime  timeUntil) {

        validateTimeRange(timeFrom, timeUntil);

        String formattedTimeFrom = timeFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedTimeUntil = timeUntil.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ApiResponse<RawTransactionDto> rawResponse = merchantApiClient.fetchTransactions(merchantOib, formattedTimeFrom, formattedTimeUntil);

        List<RawTransactionDto> rawTransactions = rawResponse.getContent();

        List<TransactionDto> transactions = rawTransactions.parallelStream()
                .map(t -> {
                    RawTransactionDetailDto detail = merchantApiClient.fetchTransactionById(t.getTransactionId());
                    System.out.println(detail);
                    return toTransactionDto(detail);
                })
                .collect(Collectors.toList());

        ApiResponse<TransactionDto> response = new ApiResponse<>();
        response.setContent(transactions);
        response.setPage(rawResponse.getPage());
        return response;
    }

    private void validateTimeRange(LocalDateTime  timeFrom, LocalDateTime  timeUntil) {
        if (timeFrom == null || timeUntil == null) {
            throw new ValidationException("timeFrom and timeUntil must not be null");
        }

        if (timeFrom.isAfter(timeUntil)) {
            throw new ValidationException("timeFrom must be before timeUntil");
        }

        long daysBetween = ChronoUnit.DAYS.between(timeFrom, timeUntil);

        if (daysBetween > 31) {
            throw new ValidationException("Time range cannot exceed 31 days");
        }
    }

    private TransactionDto toTransactionDto(RawTransactionDetailDto rawTransactionDto) {
        return TransactionDto.builder()
                .transactionId(rawTransactionDto.getTransactionId())
                .transactionType(rawTransactionDto.getTransactionType())
                .amount(rawTransactionDto.getAmount())
                .currency(rawTransactionDto.getCurrency())
                .authorizationState(rawTransactionDto.getAuthorizationState())
                .validity(rawTransactionDto.getValidity())
                .cardBrand(rawTransactionDto.getCardBrand())
                .acquirerHost(rawTransactionDto.getAcquirerHost())
                .authNumber(rawTransactionDto.getAuthNumber())
                .timestamp(rawTransactionDto.getTimestamp())
                .build();
    }
}

