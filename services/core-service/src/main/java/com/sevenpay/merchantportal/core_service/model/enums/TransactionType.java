package com.sevenpay.merchantportal.core_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType {
    SALE("sale"),
    SALE_WITH_INSTALLMENTS("sale_with_installments"),
    REFUND("refund"),
    STORNO("storno"),
    TECHNICAL_REVERSAL("technical_reversal");

    private final String name;

    TransactionType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static TransactionType getByName(String name) {
        for (TransactionType type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
