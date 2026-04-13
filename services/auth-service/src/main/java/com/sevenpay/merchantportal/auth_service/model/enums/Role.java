package com.sevenpay.merchantportal.auth_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN, USER;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }
}
