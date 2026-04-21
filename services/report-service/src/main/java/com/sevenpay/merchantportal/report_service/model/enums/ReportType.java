package com.sevenpay.merchantportal.report_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportType {
    TRANSACTIONS, SUMMARY;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ReportType fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
