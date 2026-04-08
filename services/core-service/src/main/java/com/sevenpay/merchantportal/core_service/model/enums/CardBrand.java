package com.sevenpay.merchantportal.core_service.model.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardBrand {
    MASTERCARD("mastercard"),
    VISA("visa"),
    AMEX("amex"),
    MAESTRO("maestro"),
    DINERS("diners"),
    DISCOVER("discover"),
    INVALID("invalid");

    private final String name;

    CardBrand(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static CardBrand fromJson(String value) {
        return getByName(value);
    }

    public static CardBrand getByName(String name) {
        for (CardBrand cardBrand : values()) {
            if (cardBrand.getName().equals(name)) {
                return cardBrand;
            }
        }
        return CardBrand.INVALID;
    }
}

