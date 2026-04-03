package com.sevenpay.merchantportal.user_management_service.model.common;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private List<T> content;
    private PageInfo page;
}
