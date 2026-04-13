package com.sevenpay.merchantportal.user_management_service.model.dto;

import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStatusRequest {

    @NotNull
    private Status status;
}
