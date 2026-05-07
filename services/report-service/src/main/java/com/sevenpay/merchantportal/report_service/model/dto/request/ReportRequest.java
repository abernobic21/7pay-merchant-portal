package com.sevenpay.merchantportal.report_service.model.dto.request;

import com.sevenpay.merchantportal.report_service.model.enums.PeriodType;
import com.sevenpay.merchantportal.report_service.model.enums.ReportFormat;
import com.sevenpay.merchantportal.report_service.model.enums.ReportType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    @NotNull
    @NotEmpty
    private List<String> merchantOibs;
    @NotNull
    @NotEmpty
    private List<String> mids;
    @NotNull
    private PeriodType periodType;
    @NotNull
    private LocalDateTime timeFrom;
    @NotNull
    private LocalDateTime timeUntil;
    @NotNull
    private ReportType reportType;
    @NotNull
    private ReportFormat format;
}
