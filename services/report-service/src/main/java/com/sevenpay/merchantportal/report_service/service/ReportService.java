package com.sevenpay.merchantportal.report_service.service;

import com.sevenpay.merchantportal.report_service.model.common.ReportResult;
import com.sevenpay.merchantportal.report_service.model.common.TransactionReportRow;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawSalepointDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.report_service.model.dto.request.ReportRequest;
import com.sevenpay.merchantportal.report_service.util.mapper.TransactionReportMapper;
import com.sevenpay.merchantportal.report_service.util.report.CsvReportBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final CsvReportBuilder csvReportBuilder;

    private final MerchantService merchantService;
    private final SalepointService salepointService;
    private final TransactionService transactionService;
    private final TransactionReportMapper transactionReportMapper;

    private static final String[] TRANSACTION_HEADERS = {"merchantOib", "merchantName", "mid", "salepointName", "address", "tid",
            "transactionId", "timestamp", "transactionType", "amount",
            "currency", "authorizationState", "cardBrand", "panMasked",
            "authNumber", "rrn", "responseCode"};

public ReportResult generateReport(ReportRequest request) {
        validateDateRange(request);

        byte[] content = switch (request.getReportType()) {
            case TRANSACTIONS -> generateTransactionReport(request);
            case SUMMARY -> throw new UnsupportedOperationException();
        };

        String contentType = switch (request.getFormat()) {
            case CSV -> "text/csv";
            default -> throw new UnsupportedOperationException();
        };

        return new ReportResult(content, buildFilename(request), contentType);
    }

    private byte[] generateTransactionReport(ReportRequest request) {

        List<TransactionReportRow> data = getTransactionReportData(request);

        String[][] stringifiedData = transactionReportMapper.toStringArray(data);
        return csvReportBuilder.build(stringifiedData, TRANSACTION_HEADERS);
    }

    private List<TransactionReportRow> getTransactionReportData(ReportRequest request) {
        List<String> oibs = request.getMerchantOib();
        List<String> mids = request.getMid();

        CompletableFuture<Map<String, RawMerchantDetailDto>> merchantsFuture =
                CompletableFuture.supplyAsync(() -> merchantService.getMerchants(oibs));

        CompletableFuture<Map<String, RawSalepointDto>> salepointsFuture =
                CompletableFuture.supplyAsync(() -> salepointService.getSalepoints(oibs, mids));

        CompletableFuture<List<RawTransactionDetailDto>> transactionsFuture =
                CompletableFuture.supplyAsync(() -> transactionService.getTransactions(oibs, mids, request.getTimeFrom(), request.getTimeUntil()));

        CompletableFuture.allOf(merchantsFuture, salepointsFuture, transactionsFuture).join();

        return transactionReportMapper.mapToRows(
                transactionsFuture.join(),
                merchantsFuture.join(),
                salepointsFuture.join()
        );
    }

    private String buildFilename(ReportRequest request) {
        String extension = switch (request.getFormat()) {
            case CSV -> "csv";
            case JSON -> "json";
            case PDF -> "pdf";
            default -> throw new UnsupportedOperationException();
        };

        String type = switch (request.getReportType()) {
            case TRANSACTIONS -> "transactions";
            case SUMMARY -> "summary";
        };

        DateTimeFormatter formatter = switch (request.getPeriodType()) {
            case DAILY -> DateTimeFormatter.ofPattern("yyyy-MM-dd");
            case WEEKLY,
                 MONTHLY -> DateTimeFormatter.ofPattern("yyyy-MM");
        };

        String datePart = formatter.format(request.getTimeFrom().atZone(ZoneOffset.UTC));
        return "report_" + type + "_" + datePart + "." + extension;
    }

    private void validateDateRange(ReportRequest request) {
        if (!request.getTimeFrom().isBefore(request.getTimeUntil())) {
            throw new IllegalArgumentException("timeFrom must be before timeUntil");
        }
    }
}
