package com.sevenpay.merchantportal.core_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionStateMDCProcessing {
    INCLUDED("included"),
    NOT_INCLUDED("not_included"),
    IRRELEVANT("irrelevant");

    private final String name;

    TransactionStateMDCProcessing(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static TransactionStateMDCProcessing fromJson(String value) {
        return getByName(value);
    }

    public static TransactionStateMDCProcessing getByName(String name) {
        for (TransactionStateMDCProcessing type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
