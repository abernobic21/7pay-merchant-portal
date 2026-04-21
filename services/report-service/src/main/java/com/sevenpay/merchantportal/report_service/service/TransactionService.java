package com.sevenpay.merchantportal.report_service.service;

import com.sevenpay.merchantportal.report_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.report_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final MerchantApiClient merchantApiClient;
    private final ExecutorService transactionExecutor;
    private final ExecutorService pageExecutor;

    private static final int PAGE_SIZE = 100;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<RawTransactionDetailDto> getTransactions(List<String> oibs, List<String> mids, LocalDateTime timeFrom, LocalDateTime timeUntil) {
        List<CompletableFuture<List<RawTransactionDetailDto>>> futures = oibs.stream()
                .flatMap(oib -> mids.stream().map(mid ->
                        CompletableFuture.supplyAsync(() ->
                                fetchAllTransactionsForOibMid(oib, mid, timeFrom, timeUntil), pageExecutor)))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<RawTransactionDetailDto> fetchAllTransactionsForOibMid(String oib, String mid, LocalDateTime timeFrom, LocalDateTime timeUntil) {
        String from = timeFrom.format(FORMATTER);
        String until = timeUntil.format(FORMATTER);

        ApiResponse<RawTransactionDto> firstPage = merchantApiClient
                .fetchTransactions(oib, from, until, mid, 0, PAGE_SIZE, Sort.unsorted());

        int totalPages = firstPage.getPage().getTotalPages();

        List<RawTransactionDto> allRaw = new ArrayList<>(firstPage.getContent());

        for (int page = 1; page < totalPages; page++) {
            ApiResponse<RawTransactionDto> nextPage = merchantApiClient
                    .fetchTransactions(oib, from, until, mid, page, PAGE_SIZE, Sort.unsorted());
            allRaw.addAll(nextPage.getContent());
        }

        return fetchTransactionDetails(allRaw);
    }

    private List<RawTransactionDetailDto> fetchTransactionDetails(List<RawTransactionDto> rawTransactions) {
        List<CompletableFuture<RawTransactionDetailDto>> futures = rawTransactions.stream()
                .map(t -> CompletableFuture.supplyAsync(() ->
                                merchantApiClient.fetchTransactionById(t.getTransactionId()),
                        transactionExecutor))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
