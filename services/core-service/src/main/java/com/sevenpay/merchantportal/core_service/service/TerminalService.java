package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTerminalDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerminalService {

    private final MerchantApiClient merchantApiClient;

    public ApiResponse<TerminalDto> getTerminals(String merchantOib, String mid, Pageable pageable) {

        new ApiResponse<>();
        ApiResponse<RawTerminalDto> rawResponse;
        if (merchantOib != null || mid != null) {
            rawResponse = merchantApiClient.fetchTerminalsWithFilters(merchantOib, mid, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        } else {
            rawResponse = merchantApiClient.fetchTerminals(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        }

        List<TerminalDto> terminals = rawResponse.getContent()
                .stream()
                .map(this::toTerminalDto)
                .collect(Collectors.toList());

        ApiResponse<TerminalDto> response = new ApiResponse<>();
        response.setContent(terminals);
        response.setPage(rawResponse.getPage());
        return response;
    }

    private TerminalDto toTerminalDto(RawTerminalDto rawTerminalDto) {
        return TerminalDto.builder()
                .tid(rawTerminalDto.getTid())
                .terminalType(rawTerminalDto.getTerminalType())
                .status(rawTerminalDto.getStatus())
                .midReference(rawTerminalDto.getMidReference())
                .merchantCategoryCode(rawTerminalDto.getMerchantCategoryCode())
                .currency(rawTerminalDto.getCurrency())
                .build();
    }
}


