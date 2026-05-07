package com.sevenpay.merchantportal.report_service.util.report;

import com.sevenpay.merchantportal.report_service.model.enums.ReportFormat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionReportBuilderRegistry {

    private final Map<ReportFormat, TransactionReportBuilder> builders;

    public TransactionReportBuilderRegistry(List<TransactionReportBuilder> reportBuilders) {
        builders = reportBuilders.stream()
                .collect(Collectors.toMap(
                        TransactionReportBuilder::getFormat,
                        b -> b
                ));
    }

    public TransactionReportBuilder get(ReportFormat format) {
        TransactionReportBuilder builder = builders.get(format);
        if (builder == null) {
            throw new UnsupportedOperationException("No builder for format: " + format);
        }
        return builder;
    }
}
