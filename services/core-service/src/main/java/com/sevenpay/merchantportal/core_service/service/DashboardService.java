package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.model.dto.common.RevenueByAcquirerDto;
import com.sevenpay.merchantportal.core_service.model.dto.common.RevenueByCardBrandDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.DashboardStatsDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDto;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionValidity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionService transactionService;

    public DashboardStatsDto getStats(String merchantOib, LocalDateTime timeFrom, LocalDateTime timeUntil, String mid, String tid) {
        List<TransactionDto> transactions = transactionService.getAllTransactions(merchantOib, timeFrom, timeUntil, mid, tid);

        DashboardStatsDto stats = calculateStats(transactions);

        stats.setCachedAt(null);
        return stats;
    }

    public DashboardStatsDto calculateStats(List<TransactionDto> transactions) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalCount = 0;
        HashMap<String, RevenueByCardBrandDto> byCardBrand = new HashMap<>();
        HashMap<String, RevenueByAcquirerDto> byAcquirer = new HashMap<>();

        for (TransactionDto t : transactions) {
            if (!isRevenueTransaction(t)) continue;

            totalRevenue = totalRevenue.add(t.getAmount());
            totalCount++;

            String cardBrand = t.getCardBrand().getName();
            byCardBrand.merge(cardBrand, RevenueByCardBrandDto.builder().cardBrand(cardBrand).revenue(t.getAmount()).count(1).build(), (existing, newVal) -> {
                existing.setRevenue(existing.getRevenue().add(t.getAmount()));
                existing.setCount(existing.getCount() + 1);
                return existing;
            });

            String acquirer = t.getAcquirerHost();
            byAcquirer.merge(acquirer, RevenueByAcquirerDto.builder().acquirerHost(acquirer).revenue(t.getAmount()).count(1).build(), (existing, newVal) -> {
                existing.setRevenue(existing.getRevenue().add(t.getAmount()));
                existing.setCount(existing.getCount() + 1);
                return existing;
            });
        }
        BigDecimal avg = totalCount > 0 ? totalRevenue.divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        return DashboardStatsDto.builder().totalRevenue(totalRevenue).totalTransactions(totalCount).avgTransactionValue(avg).revenueByCardBrand(new ArrayList<>(byCardBrand.values())).revenueByAcquirer(new ArrayList<>(byAcquirer.values())).build();
    }

    private boolean isRevenueTransaction(TransactionDto t) {
        return t.getAuthorizationState() == TransactionAuthorizationState.AUTHORIZED && t.getValidity() == TransactionValidity.VALID && (t.getTransactionType() == TransactionType.SALE || t.getTransactionType() == TransactionType.SALE_WITH_INSTALLMENTS);
    }
}
