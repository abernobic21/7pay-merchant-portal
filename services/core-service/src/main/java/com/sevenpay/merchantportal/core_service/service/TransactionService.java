package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.exception.ValidationException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTransactionDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDto;
import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import com.sevenpay.merchantportal.core_service.util.ApiResponseBuilder;
import com.sevenpay.merchantportal.core_service.util.TransactionListProcessor;
import com.sevenpay.merchantportal.core_service.util.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    private final RedisCacheService cacheService;
    private static final Duration TTL = Duration.ofMinutes(2);

    public ApiResponse<TransactionDto> fetchTransactions(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil, String mid, String tid, int page, int size, Sort sort) {

        String formattedTimeFrom = timeFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedTimeUntil = timeUntil.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ApiResponse<RawTransactionDto> rawResponse = merchantApiClient.fetchTransactions(merchantOib, formattedTimeFrom, formattedTimeUntil, mid, tid, page, size, sort);

        List<TransactionDto> transactions = fetchTransactionDetails(rawResponse.getContent());

        ApiResponse<TransactionDto> response = new ApiResponse<>();
        response.setContent(transactions);
        response.setPage(rawResponse.getPage());
        return response;
    }

    private List<TransactionDto> fetchTransactionDetails(List<RawTransactionDto> rawTransactions) {

        List<CompletableFuture<TransactionDto>> futures = rawTransactions.stream()
                .map(t -> CompletableFuture.supplyAsync(() -> {
                    RawTransactionDetailDto detail =
                            merchantApiClient.fetchTransactionById(t.getTransactionId());
                    return TransactionMapper.toTransactionDto(detail);
                }, transactionExecutor))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public ApiResponse<TransactionDto> getTransactions(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil, String mid, String tid, TransactionAuthorizationState status, TransactionType transactionType, CardBrand cardBrand, String authNumber, String rrn, Pageable pageable) {

        validateTimeRange(timeFrom, timeUntil);

        if (!TransactionListProcessor.requiresLocalFiltering(status, transactionType, cardBrand, authNumber, rrn)) {
            return handlePaginatedFetch(merchantOib, timeFrom, timeUntil, mid, tid, pageable);
        }

        return handleFetchWithLocalFilters(merchantOib, timeFrom, timeUntil, mid, tid,
                status, transactionType, cardBrand, authNumber, rrn, pageable);
    }

    private ApiResponse<TransactionDto> handlePaginatedFetch(String merchantOib, LocalDateTime timeFrom,
                                                             LocalDateTime timeUntil, String mid, String tid, Pageable pageable) {

        String paginatedKey = buildPaginatedKey(merchantOib, timeFrom, timeUntil, mid, tid, pageable);
        ApiResponse<TransactionDto> cached = cacheService.get(paginatedKey);
        if (cached != null) {
            return cached;
        }

        String rawKey = buildRawKey(merchantOib, timeFrom, timeUntil, mid, tid);
        List<TransactionDto> rawCached = cacheService.get(rawKey);
        if (rawCached != null) {
            List<TransactionDto> sorted = TransactionListProcessor.sort(rawCached, pageable.getSort());
            ApiResponse<TransactionDto> response = ApiResponseBuilder.build(sorted, pageable);
            cacheService.set(paginatedKey, response, TTL);
            return response;
        }

        ApiResponse<TransactionDto> response = fetchTransactions(merchantOib, timeFrom, timeUntil,
                mid, tid, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        cacheService.set(paginatedKey, response, TTL);
        return response;
    }

    private ApiResponse<TransactionDto> handleFetchWithLocalFilters(String merchantOib, LocalDateTime timeFrom,
                                                                    LocalDateTime timeUntil, String mid, String tid, TransactionAuthorizationState status,
                                                                    TransactionType transactionType, CardBrand cardBrand, String authNumber, String rrn,
                                                                    Pageable pageable) {

        String rawKey = buildRawKey(merchantOib, timeFrom, timeUntil, mid, tid);
        List<TransactionDto> allTransactions = cacheService.get(rawKey);
        if (allTransactions == null) {
            allTransactions = getAllTransactions(merchantOib, timeFrom, timeUntil, mid, tid);
            cacheService.set(rawKey, allTransactions, TTL);
        }

        List<TransactionDto> filtered = TransactionListProcessor.filter(allTransactions, status,
                transactionType, cardBrand, authNumber, rrn);
        List<TransactionDto> sorted = TransactionListProcessor.sort(filtered, pageable.getSort());
        return ApiResponseBuilder.build(sorted, pageable);
    }

    public List<TransactionDto> getAllTransactions(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil, String mid, String tid) {
        final int maxSize = 100;

        ApiResponse<TransactionDto> firstResponse =
                fetchTransactions(merchantOib, timeFrom, timeUntil, mid, tid, 0, maxSize, null);

        int totalPages = firstResponse.getPage().getTotalPages();

        List<CompletableFuture<List<TransactionDto>>> futures =
                IntStream.range(1, totalPages)
                        .mapToObj(page -> CompletableFuture.supplyAsync(() ->
                                        fetchTransactions(merchantOib, timeFrom, timeUntil, mid, tid, page, maxSize, null)
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


    public TransactionDetailDto getTransactionById(String transactionId) {
        String key = "transactions:id_" + transactionId;

        TransactionDetailDto cached = cacheService.get(key);
        if (cached != null) {
            return cached;
        }

        RawTransactionDetailDto rawTransaction = merchantApiClient.fetchTransactionById(transactionId);
        TransactionDetailDto transaction = TransactionMapper.toTransactionDetailDto(rawTransaction);
        cacheService.set(key, transaction, TTL);
        return transaction;
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

    private String buildPaginatedKey(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil,
                                     String mid, String tid, Pageable pageable) {
        return String.format("transactions:paginated:%s:from_%s:until_%s:mid_%s:tid_%s:page_%d:size_%d:sort_%s",
                merchantOib,
                timeFrom != null ? timeFrom : "all",
                timeUntil != null ? timeUntil : "all",
                mid != null ? mid : "all",
                tid != null ? tid : "all",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    private String buildRawKey(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil,
                               String mid, String tid) {
        return String.format("transactions:raw:%s:from_%s:until_%s:mid_%s:tid_%s",
                merchantOib,
                timeFrom != null ? timeFrom : "all",
                timeUntil != null ? timeUntil : "all",
                mid != null ? mid : "all",
                tid != null ? tid : "all"
        );
    }
}
