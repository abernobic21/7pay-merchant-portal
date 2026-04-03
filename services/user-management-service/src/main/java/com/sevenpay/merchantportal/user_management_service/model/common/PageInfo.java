package com.sevenpay.merchantportal.user_management_service.model.common;

import lombok.Data;

@Data
public class PageInfo {
    private Integer totalPages;
    private Integer totalElements;
    private Integer size;
    private Integer number;
}
