package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.exception.ValidationException;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.common.Page;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTransactionDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDto;
import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionValidity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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

    public ApiResponse<TransactionDto> fetchTransactions(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil, String mid, String tid, int page, int size, Sort sort) {

        validateTimeRange(timeFrom, timeUntil);

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
                    return toTransactionDto(detail);
                }, transactionExecutor))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }


    public ApiResponse<TransactionDto> getTransactions(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil, String mid, String tid, TransactionAuthorizationState status, TransactionType transactionType, CardBrand cardBrand, String authNumber, String rnn, Pageable pageable) {

        if(!requiresLocalFiltering(status, transactionType, cardBrand, authNumber, rnn)){
            ApiResponse<TransactionDto> transactionResponse = fetchTransactions(merchantOib, timeFrom, timeUntil, mid, tid, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

            List<TransactionDto> sortedTransactions = applySorting(transactionResponse.getContent(), pageable.getSort());

            return buildResponse(sortedTransactions, pageable);
        }

        List<TransactionDto> allTransactions = getAllTransactions(merchantOib, timeFrom, timeUntil, mid, tid);

        List<TransactionDto> filteredTransactions = allTransactions.stream()
                .filter(t -> status == null || t.getAuthorizationState() == status)
                .filter(t -> transactionType == null || t.getTransactionType() == transactionType)
                .filter(t -> cardBrand == null || t.getCardBrand() == cardBrand)
                .filter(t -> authNumber == null || authNumber.equals(t.getAuthNumber()))
                .filter(t -> rnn == null || rnn.equals(t.getRrn()))
                .toList();

        List<TransactionDto> sortedTransactions = applySorting(filteredTransactions, pageable.getSort());

        return buildResponse(sortedTransactions, pageable);
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

    private boolean requiresLocalFiltering(
            TransactionAuthorizationState status,
            TransactionType transactionType,
            CardBrand cardBrand,
            String authNumber,
            String rnn
    ) {
        return status != null
                || transactionType != null
                || cardBrand != null
                || authNumber != null
                || rnn != null;
    }

    private List<TransactionDto> applySorting(List<TransactionDto> transactions, Sort sort) {
        if (sort.isUnsorted()) return transactions;

        Comparator<TransactionDto> comparator = sort.stream()
                .map(order -> {
                    Comparator<TransactionDto> c = switch (order.getProperty()) {
                        case "transactionId" -> Comparator.comparing(TransactionDto::getTransactionId);
                        case "transactionType" -> Comparator.comparing(t -> t.getTransactionType().name());
                        case "amount" -> Comparator.comparing(TransactionDto::getAmount);
                        case "currency" -> Comparator.comparing(TransactionDto::getCurrency);
                        case "authorizationState" -> Comparator.comparing(t -> t.getAuthorizationState().name());
                        case "validity" -> Comparator.comparing(t -> t.getValidity().name());
                        case "cardBrand" -> Comparator.comparing(t -> t.getCardBrand().name());
                        case "acquirerHost" -> Comparator.comparing(TransactionDto::getAcquirerHost);
                        case "authNumber" -> Comparator.comparing(TransactionDto::getAuthNumber);
                        case "rrn" -> Comparator.comparing(TransactionDto::getRrn,
                                Comparator.nullsLast(Comparator.naturalOrder()));
                        case "timestamp" -> Comparator.comparing(TransactionDto::getTimestamp);
                        default -> throw new ValidationException("Unsupported sort field: " + order.getProperty());
                    };
                    return order.isAscending() ? c : c.reversed();
                })
                .reduce(Comparator::thenComparing)
                .orElseThrow();

        return transactions.stream().sorted(comparator).collect(Collectors.toList());
    }


    private ApiResponse<TransactionDto> buildResponse(List<TransactionDto> transactions, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), transactions.size());
        List<TransactionDto> pageTransactions = transactions.subList(start, end);

        Page page = Page.builder()
                .totalPages((int) Math.ceil((double) transactions.size() / pageable.getPageSize()))
                .totalElements(transactions.size())
                .size(pageable.getPageSize())
                .number(pageable.getPageNumber())
                .build();

        ApiResponse<TransactionDto> response = new ApiResponse<>();
        response.setContent(pageTransactions);
        response.setPage(page);

        return response;
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
                .rrn(rawDto.getRrn())
                .timestamp(rawDto.getTimestamp())
                .build();
    }
}

