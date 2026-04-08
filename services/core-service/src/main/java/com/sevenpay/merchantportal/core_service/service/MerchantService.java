package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.common.Page;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantApiClient merchantApiClient;

    public ApiResponse<MerchantDto> fetchMerchants(int page, int size, Sort sort) {

        ApiResponse<RawMerchantDto> rawResponse = merchantApiClient.fetchMerchants(page, size, sort);

        List<MerchantDto> merchants = rawResponse.getContent()
                .stream()
                .map(this::toMerchantDto)
                .collect(Collectors.toList());

        ApiResponse<MerchantDto> response = new ApiResponse<>();
        response.setContent(merchants);
        response.setPage(rawResponse.getPage());
        return response;
    }

    public ApiResponse<MerchantDto> getMerchants(MerchantStatus status, Pageable pageable) {

        if (status == null)
            return fetchMerchants(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<MerchantDto> allMerchants = getAllMerchants(pageable.getSort());

        List<MerchantDto> filteredMerchants = allMerchants.stream()
                .filter(m -> m.getStatus() == status)
                .toList();

        return buildResponse(filteredMerchants, pageable);

    }

    public List<MerchantDto> getAllMerchants(Sort sort) {
        final int maxSize = 100;

        ApiResponse<MerchantDto> firstResponse =
                fetchMerchants(0, maxSize, sort );

        int totalPages = firstResponse.getPage().getTotalPages();

        List<MerchantDto> allMerchants = new ArrayList<>(firstResponse.getContent());

        for(int i = 1; i < totalPages; i++){
            ApiResponse<MerchantDto> response = fetchMerchants(i, maxSize, sort);
            allMerchants.addAll(response.getContent());
        }
        return allMerchants;
    }

    public MerchantDetailDto getMerchantByOib(String oib) {
        RawMerchantDetailDto rawMerchant = merchantApiClient.fetchMerchantByOib(oib);

        return toMerchantDetailDto(rawMerchant);
    }

    private ApiResponse<MerchantDto> buildResponse(List<MerchantDto> merchants, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), merchants.size());
        List<MerchantDto> pageMerchants = merchants.subList(start, end);

        Page page = Page.builder()
                .totalPages((int) Math.ceil((double) merchants.size() / pageable.getPageSize()))
                .totalElements(merchants.size())
                .size(pageable.getPageSize())
                .number(pageable.getPageNumber())
                .build();

        ApiResponse<MerchantDto> response = new ApiResponse<>();
        response.setContent(pageMerchants);
        response.setPage(page);

        return response;
    }

    private MerchantDto toMerchantDto(RawMerchantDto rawMerchantDto) {
        return MerchantDto.builder()
                .shortName(rawMerchantDto.getShortName())
                .fullName(rawMerchantDto.getFullName())
                .oib(rawMerchantDto.getOib())
                .status(MerchantStatus.getByName(rawMerchantDto.getStatus()))
                .address(rawMerchantDto.getAddress())
                .build();
    }

    private MerchantDetailDto toMerchantDetailDto(RawMerchantDetailDto rawMerchantDto) {
        return MerchantDetailDto.builder()
                .shortName(rawMerchantDto.getShortName())
                .fullName(rawMerchantDto.getFullName())
                .oib(rawMerchantDto.getOib())
                .address(rawMerchantDto.getAddress())
                .status(MerchantStatus.getByName(rawMerchantDto.getStatus()))
                .supportedTransactionTypes(rawMerchantDto.getSupportedTransactionTypes())
                .supportedAcquirers(rawMerchantDto.getSupportedAcquirers())
                .supportedCardBrands(rawMerchantDto.getSupportedCardBrands())
                .build();
    }
}
