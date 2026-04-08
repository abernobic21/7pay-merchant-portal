package com.sevenpay.merchantportal.core_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SalepointStatus {
    ACTIVE("active"),
    DEACTIVATED("deactivated"),
    HOLD("hold");

    private final String name;

    SalepointStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static SalepointStatus getByName(String name) {
        for (SalepointStatus merchantStatus : values()) {
            if (merchantStatus.getName().equals(name)) {
                return merchantStatus;
            }
        }
        return null;
    }
}
