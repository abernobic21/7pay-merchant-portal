package com.sevenpay.merchantportal.report_service.util.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sevenpay.merchantportal.report_service.model.common.TransactionReportRow;
import com.sevenpay.merchantportal.report_service.model.enums.ReportFormat;
import com.sevenpay.merchantportal.report_service.util.mapper.TransactionReportMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonReportBuilder implements TransactionReportBuilder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] build(List<TransactionReportRow> data, String[] headers) {
        try {
            String[][] stringData = TransactionReportMapper.toStringArray(data);

            List<Map<String, String>> rows = new ArrayList<>();
            for (String[] row : stringData) {
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    map.put(headers[i], i < row.length ? row[i] : "");
                }
                rows.add(map);
            }

            return objectMapper.writeValueAsBytes(rows);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build JSON report", e);
        }
    }

    @Override
    public ReportFormat getFormat() {
        return ReportFormat.JSON;
    }
}
