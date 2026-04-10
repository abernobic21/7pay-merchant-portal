package com.sevenpay.merchantportal.core_service.util;

import com.sevenpay.merchantportal.core_service.exception.ValidationException;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDto;
import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionListProcessor {
    private TransactionListProcessor() {}

    public static List<TransactionDto> filter(List<TransactionDto> transactions,
                                       TransactionAuthorizationState status,
                                       TransactionType transactionType,
                                       CardBrand cardBrand,
                                       String authNumber,
                                       String rrn) {
        return transactions.stream()
                .filter(t -> status == null || t.getAuthorizationState() == status)
                .filter(t -> transactionType == null || t.getTransactionType() == transactionType)
                .filter(t -> cardBrand == null || t.getCardBrand() == cardBrand)
                .filter(t -> authNumber == null || authNumber.equals(t.getAuthNumber()))
                .filter(t -> rrn == null || rrn.equals(t.getRrn()))
                .toList();
    }

    public static boolean requiresLocalFiltering(TransactionAuthorizationState status,
                                          TransactionType transactionType,
                                          CardBrand cardBrand,
                                          String authNumber,
                                          String rrn) {
        return status != null
                || transactionType != null
                || cardBrand != null
                || authNumber != null
                || rrn != null;
    }

    public static List<TransactionDto> sort(List<TransactionDto> transactions, Sort sort) {
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
}
