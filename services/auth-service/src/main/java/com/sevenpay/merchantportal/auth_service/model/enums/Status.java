package com.sevenpay.merchantportal.auth_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    ACTIVE, DEACTIVATED;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static Status fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
