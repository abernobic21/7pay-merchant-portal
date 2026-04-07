package com.sevenpay.merchantportal.user_management_service.model.dto;

import com.sevenpay.merchantportal.user_management_service.model.enums.AccessLevel;
import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private AccessLevel accessLevel;
    private String merchantId;
    private Status status;
}

