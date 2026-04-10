package com.sevenpay.merchantportal.core_service.util;

import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.common.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class ApiResponseBuilder {
    private ApiResponseBuilder() {
    }

    public static <T> ApiResponse<T> build(List<T> items, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), items.size());
        List<T> pageContent = start >= items.size() ? List.of() : new ArrayList<>(items.subList(start, end));

        Page page = Page.builder()
                .totalPages((int) Math.ceil((double) items.size() / pageable.getPageSize()))
                .totalElements(items.size())
                .size(pageable.getPageSize())
                .number(pageable.getPageNumber())
                .build();

        ApiResponse<T> response = new ApiResponse<>();
        response.setContent(pageContent);
        response.setPage(page);
        return response;
    }
}
