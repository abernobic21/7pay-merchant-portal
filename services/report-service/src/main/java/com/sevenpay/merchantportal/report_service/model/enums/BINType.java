package com.sevenpay.merchantportal.report_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BINType {
    ON_US("on_us"),
    OFF_US("off_us");

    private final String name;

    BINType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static BINType fromJson(String value) {
        return getByName(value);
    }

    public static BINType getByName(String name) {
        for (BINType binType : values()) {
            if (binType.getName().equals(name)) {
                return binType;
            }
        }
        return null;
    }
}
