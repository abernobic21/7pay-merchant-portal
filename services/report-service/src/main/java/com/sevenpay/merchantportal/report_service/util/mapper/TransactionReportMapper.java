package com.sevenpay.merchantportal.report_service.util.mapper;

import com.sevenpay.merchantportal.report_service.model.common.TransactionReportRow;
import com.sevenpay.merchantportal.report_service.model.dto.common.AddressDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawSalepointDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.report_service.model.enums.PeriodType;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionReportMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<TransactionReportRow> mapToRows(
            List<RawTransactionDetailDto> transactions,
            Map<String, RawMerchantDetailDto> merchants,
            Map<String, RawSalepointDto> salepoints,
            PeriodType periodType) {

        return transactions.stream()
                .map(tx -> buildRow(tx, merchants, salepoints, periodType))
                .sorted(Comparator.comparing(TransactionReportRow::getPeriod)
                        .thenComparing(TransactionReportRow::getMerchantOib)
                        .thenComparing(TransactionReportRow::getMid)
                        .thenComparing(TransactionReportRow::getTid)
                        .thenComparing(TransactionReportRow::getTimestamp))
                .collect(Collectors.toList());
    }

    public static String[][] toStringArray(List<TransactionReportRow> rows) {
        return rows.stream()
                .map(row -> new String[]{
                        nullSafe(row.getPeriod()),
                        nullSafe(row.getMerchantOib()),
                        nullSafe(row.getMerchantName()),
                        nullSafe(row.getMid()),
                        nullSafe(row.getSalepointName()),
                        nullSafe(row.getAddress()),
                        nullSafe(row.getTid()),
                        nullSafe(row.getTransactionId()),
                        row.getTimestamp() != null ? row.getTimestamp().format(FORMATTER) : "",
                        nullSafe(row.getTransactionType()),
                        row.getAmount() != null ? row.getAmount().toPlainString() : "",
                        nullSafe(row.getCurrency()),
                        nullSafe(row.getAuthorizationState()),
                        nullSafe(row.getCardBrand()),
                        nullSafe(row.getPanMasked()),
                        nullSafe(row.getAuthNumber()),
                        nullSafe(row.getRrn()),
                        nullSafe(row.getResponseCode())
                })
                .toArray(String[][]::new);
    }

    private static String nullSafe(String value) {
        return value != null ? value : "";
    }

    private TransactionReportRow buildRow(
            RawTransactionDetailDto tx,
            Map<String, RawMerchantDetailDto> merchants,
            Map<String, RawSalepointDto> salepoints,
            PeriodType periodType) {

        RawSalepointDto salepoint = salepoints.get(tx.getMid());
        String merchantOib = salepoint != null ? salepoint.getMerchantOib() : "";
        RawMerchantDetailDto merchant = merchants.get(merchantOib);

        return TransactionReportRow.builder()
                .period(computePeriod(tx.getTimestamp(), periodType))
                .merchantOib(merchantOib)
                .merchantName(merchant != null ? merchant.getShortName() : "")
                .mid(tx.getMid())
                .salepointName(salepoint != null ? salepoint.getName() : "")
                .address(formatAddress(salepoint))
                .tid(tx.getTid())
                .transactionId(tx.getTransactionId())
                .timestamp(tx.getTimestamp())
                .transactionType(tx.getTransactionType())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .authorizationState(tx.getAuthorizationState())
                .cardBrand(tx.getCardBrand())
                .panMasked(tx.getPanMasked())
                .authNumber(tx.getAuthNumber())
                .rrn(tx.getRrn())
                .responseCode(tx.getResponseCode())
                .build();
    }

    private String computePeriod(LocalDateTime timestamp, PeriodType periodType) {
        if (timestamp == null) return "";
        LocalDate date = timestamp.toLocalDate();
        return switch (periodType) {
            case DAILY   -> date.toString();
            case WEEKLY  -> {
                LocalDate monday = date.with(DayOfWeek.MONDAY);
                LocalDate sunday = date.with(DayOfWeek.SUNDAY);
                yield monday + " / " + sunday;
            }
            case MONTHLY -> date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        };
    }

    private String formatAddress(RawSalepointDto salepoint) {
        if (salepoint == null || salepoint.getAddress() == null) return "";
        AddressDto address = salepoint.getAddress();
        return nullSafe(address.getStreetAddressNumber()) + ", " + nullSafe(address.getCity());
    }
}
