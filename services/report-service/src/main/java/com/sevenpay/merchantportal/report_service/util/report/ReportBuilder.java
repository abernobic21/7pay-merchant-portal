package com.sevenpay.merchantportal.report_service.util.report;

public interface ReportBuilder {
    byte[] build(String[][] data, String[] headers);
    String getContentType();
    String getFileExtension();
}
