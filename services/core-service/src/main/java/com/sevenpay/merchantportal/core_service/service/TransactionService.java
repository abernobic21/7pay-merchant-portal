package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.exception.ValidationException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTransactionDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDto;
import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionValidity;
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

    public ApiResponse<TransactionDto> getTransactions(String merchantOib, LocalDateTime  timeFrom, LocalDateTime  timeUntil, int page, int size) {

        validateTimeRange(timeFrom, timeUntil);

        String formattedTimeFrom = timeFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedTimeUntil = timeUntil.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ApiResponse<RawTransactionDto> rawResponse = merchantApiClient.fetchTransactions(merchantOib, formattedTimeFrom, formattedTimeUntil, page, size);

        List<RawTransactionDto> rawTransactions = rawResponse.getContent();

        List<TransactionDto> transactions = rawTransactions.parallelStream()
                .map(t -> {
                    RawTransactionDetailDto detail = merchantApiClient.fetchTransactionById(t.getTransactionId());
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

    private TransactionDto toTransactionDto(RawTransactionDetailDto rawDto) {
        return TransactionDto.builder()
                .transactionId(rawDto.getTransactionId())
                .transactionType(TransactionType.getByName(rawDto.getTransactionType()))
                .amount(rawDto.getAmount())
                .currency(rawDto.getCurrency())
                .authorizationState(TransactionAuthorizationState.getByName(rawDto.getAuthorizationState()))
                .validity(TransactionValidity.getByName(rawDto.getValidity()))
                .cardBrand(CardBrand.getByName(rawDto.getCardBrand()))
                .acquirerHost(rawDto.getAcquirerHost())
                .authNumber(rawDto.getAuthNumber())
                .timestamp(rawDto.getTimestamp())
                .build();
    }
}

