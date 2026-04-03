package com.sevenpay.merchantportal.user_management_service.model.dto;

import com.sevenpay.merchantportal.user_management_service.model.enums.AccessLevel;
import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserDto {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private Role role;
        private AccessLevel accessLevel;
        private String merchantId;
        private Status status;
        private Boolean firstLoginFlag;
        private LocalDateTime createdAt;
    }

