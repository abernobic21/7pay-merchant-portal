package com.sevenpay.merchantportal.report_service.service;

import com.sevenpay.merchantportal.report_service.model.common.ReportResult;
import com.sevenpay.merchantportal.report_service.model.common.TransactionReportRow;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawSalepointDto;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.report_service.model.dto.request.ReportRequest;
import com.sevenpay.merchantportal.report_service.util.mapper.TransactionReportMapper;
import com.sevenpay.merchantportal.report_service.util.report.TransactionReportBuilder;
import com.sevenpay.merchantportal.report_service.util.report.TransactionReportBuilderRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final TransactionReportBuilderRegistry reportBuilderRegistry;

    private final MerchantService merchantService;
    private final SalepointService salepointService;
    private final TransactionService transactionService;
    private final TransactionReportMapper transactionReportMapper;

    private static final String[] TRANSACTION_HEADERS = {
            "period", "merchantOib", "merchantName", "mid", "salepointName", "address", "tid",
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
            case JSON -> "text/json";
            default -> throw new UnsupportedOperationException();
        };

        return new ReportResult(content, buildFilename(request), contentType);
    }

    private byte[] generateTransactionReport(ReportRequest request) {

        List<TransactionReportRow> data = getTransactionReportData(request);

        TransactionReportBuilder builder = reportBuilderRegistry.get(request.getFormat());
        return builder.build(data, TRANSACTION_HEADERS);
    }

    private List<TransactionReportRow> getTransactionReportData(ReportRequest request) {
        List<String> oibs = request.getMerchantOibs();
        List<String> mids = request.getMids();

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
                salepointsFuture.join(),
                request.getPeriodType()
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStart = formatter.format(request.getTimeFrom());
        String dateEnd = formatter.format(request.getTimeFrom());

        return "report_" + type + "_" + dateStart + "_" + dateEnd + "_" + request.getPeriodType() + "." + extension;
    }

    private void validateDateRange(ReportRequest request) {
        if (!request.getTimeFrom().isBefore(request.getTimeUntil())) {
            throw new IllegalArgumentException("Time from must be before time until");
        }
    }
}
