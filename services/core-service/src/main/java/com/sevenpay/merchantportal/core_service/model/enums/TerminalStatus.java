package com.sevenpay.merchantportal.core_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TerminalStatus {
    PREPARED("prepared"),
    AUTHENTICATED("authenticated"),
    ACTIVE("active"),
    DEACTIVATED("deactivated"),
    SUSPENDED("suspended"),
    BLOCKED("blocked");

    private final String name;

    TerminalStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static TerminalStatus getByName(String name) {
        for (TerminalStatus merchantStatus : values()) {
            if (merchantStatus.getName().equals(name)) {
                return merchantStatus;
            }
        }
        return null;
    }
}
