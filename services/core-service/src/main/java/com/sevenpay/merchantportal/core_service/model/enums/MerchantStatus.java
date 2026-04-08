package com.sevenpay.merchantportal.core_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MerchantStatus {
    ACTIVE("active"),
    DEACTIVATED("deactivated"),
    HOLD("hold");

    private final String name;

    MerchantStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static MerchantStatus getByName(String name) {
        for (MerchantStatus merchantStatus : values()) {
            if (merchantStatus.getName().equals(name)) {
                return merchantStatus;
            }
        }
        return null;
    }
}
