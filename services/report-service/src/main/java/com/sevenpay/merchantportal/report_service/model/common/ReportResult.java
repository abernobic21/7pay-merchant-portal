package com.sevenpay.merchantportal.report_service.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResult {
    private byte[] content;
    private String filename;
    private String contentType;
}
