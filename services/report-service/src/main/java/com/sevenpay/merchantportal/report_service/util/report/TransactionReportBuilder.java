package com.sevenpay.merchantportal.report_service.util.report;

import com.sevenpay.merchantportal.report_service.model.common.TransactionReportRow;
import com.sevenpay.merchantportal.report_service.model.enums.ReportFormat;

import java.util.List;

public interface TransactionReportBuilder {
    byte[] build(List<TransactionReportRow> data, String[] headers);
    ReportFormat getFormat();
}
