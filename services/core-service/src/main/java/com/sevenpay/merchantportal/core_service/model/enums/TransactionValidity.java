package com.sevenpay.merchantportal.core_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionValidity {
    VALID("valid"),
    VOIDED("voided"),
    CANCELED("cancelled");

    private final String name;

    TransactionValidity(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static TransactionValidity getByName(String name) {
        for (TransactionValidity validity : values()) {
            if (validity.getName().equals(name)) {
                return validity;
            }
        }
        return null;
    }
}

