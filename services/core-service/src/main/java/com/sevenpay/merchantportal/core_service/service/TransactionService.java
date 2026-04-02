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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final MerchantApiClient merchantApiClient;
    private final ExecutorService transactionExecutor;
    private final ExecutorService pageExecutor;

    public ApiResponse<TransactionDto> getTransactions(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil, int page, int size) {

        validateTimeRange(timeFrom, timeUntil);

        String formattedTimeFrom = timeFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedTimeUntil = timeUntil.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ApiResponse<RawTransactionDto> rawResponse = merchantApiClient.fetchTransactions(merchantOib, formattedTimeFrom, formattedTimeUntil, page, size);

        List<TransactionDto> transactions = fetchTransactionDetails(rawResponse.getContent());

        ApiResponse<TransactionDto> response = new ApiResponse<>();
        response.setContent(transactions);
        response.setPage(rawResponse.getPage());
        return response;
    }

    public List<TransactionDto> getAllTransactions(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil) {
        final int maxSize = 100;

        ApiResponse<TransactionDto> firstResponse =
                getTransactions(merchantOib, timeFrom, timeUntil, 0, maxSize);

        int totalPages = firstResponse.getPage().getTotalPages();

        List<CompletableFuture<List<TransactionDto>>> futures =
                IntStream.range(1, totalPages)
                        .mapToObj(page -> CompletableFuture.supplyAsync(() ->
                                                getTransactions(merchantOib, timeFrom, timeUntil, page, maxSize)
                                                .getContent(),
                                pageExecutor)
                        )
                        .toList();

        List<TransactionDto> allTransactions = new ArrayList<>(firstResponse.getContent());

        futures.stream()
                .map(CompletableFuture::join)
                .forEach(allTransactions::addAll);

        return allTransactions;
    }

    private void validateTimeRange(LocalDateTime timeFrom, LocalDateTime timeUntil) {
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

    private List<TransactionDto> fetchTransactionDetails(List<RawTransactionDto> rawTransactions) {

        List<CompletableFuture<TransactionDto>> futures = rawTransactions.stream()
                .map(t -> CompletableFuture.supplyAsync(() -> {
                    RawTransactionDetailDto detail =
                            merchantApiClient.fetchTransactionById(t.getTransactionId());
                    return toTransactionDto(detail);
                }, transactionExecutor))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
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

