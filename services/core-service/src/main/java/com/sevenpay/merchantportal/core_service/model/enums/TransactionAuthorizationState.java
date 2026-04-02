package com.sevenpay.merchantportal.core_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionAuthorizationState {
    AUTHORIZED("authorized"),
    REJECTED("rejected"),
    NO_RESPONSE("no_response");

    private final String name;

    TransactionAuthorizationState(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static TransactionAuthorizationState getByName(String name) {
        for (TransactionAuthorizationState authorizationState : values()) {
            if (authorizationState.getName().equals(name)) {
                return authorizationState;
            }
        }
        return null;
    }
}

