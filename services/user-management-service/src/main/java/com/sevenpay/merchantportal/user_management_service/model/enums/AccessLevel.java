package com.sevenpay.merchantportal.user_management_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccessLevel {
    MERCHANT, BRANCH;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static AccessLevel fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
