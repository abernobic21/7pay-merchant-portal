package com.sevenpay.merchantportal.report_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PeriodType {
    DAILY, WEEKLY, MONTHLY;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static PeriodType fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
