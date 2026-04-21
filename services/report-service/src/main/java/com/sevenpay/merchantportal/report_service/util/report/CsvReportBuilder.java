package com.sevenpay.merchantportal.report_service.util.report;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@Component
public class CsvReportBuilder implements ReportBuilder{
    @Override
    public byte[] build(String[][] data, String[] headers) {
        try {
            StringWriter sw = new StringWriter();
            BufferedWriter writer = new BufferedWriter(sw);

            writer.write(toCsvRow(headers));
            writer.newLine();

            for (String[] row : data) {
                writer.write(toCsvRow(row));
                writer.newLine();
            }

            writer.flush();
            return sw.toString().getBytes(StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Failed to build CSV report", e);
        }
    }

    private String toCsvRow(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(escapeCsvField(fields[i]));
        }
        return sb.toString();
    }

    private String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    @Override public String getContentType()   { return "text/csv"; }
    @Override public String getFileExtension() { return "csv"; }
}
