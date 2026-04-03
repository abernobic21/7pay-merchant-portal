package com.sevenpay.merchantportal.user_management_service.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN, USER;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }
}
