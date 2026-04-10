package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import com.sevenpay.merchantportal.core_service.util.mapper.MerchantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantApiClient merchantApiClient;
    private final RedisCacheService cacheService;
    private static final Duration TTL = Duration.ofMinutes(15);

    public ApiResponse<MerchantDto> getMerchants(MerchantStatus status, Pageable pageable) {
        String key = buildCacheKey(status, pageable);

        ApiResponse<MerchantDto> cached = cacheService.get(key);
        if (cached != null) {
            return cached;
        }

        ApiResponse<RawMerchantDto> rawResponse = status != null
                ? merchantApiClient.fetchMerchantsByStatus(status, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort())
                : merchantApiClient.fetchMerchants(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<MerchantDto> merchants = rawResponse.getContent()
                .stream()
                .map(MerchantMapper::toMerchantDto)
                .collect(Collectors.toList());

        ApiResponse<MerchantDto> response = new ApiResponse<>();
        response.setContent(merchants);
        response.setPage(rawResponse.getPage());

        cacheService.set(key, response, TTL);
        return response;
    }

    public MerchantDetailDto getMerchantByOib(String oib) {
        String key = "merchant:" + oib;

        MerchantDetailDto cached = cacheService.get(key);
        if(cached != null){
            return cached;
        }

        RawMerchantDetailDto rawMerchant = merchantApiClient.fetchMerchantByOib(oib);

        MerchantDetailDto merchant = MerchantMapper.toMerchantDetailDto(rawMerchant);
        cacheService.set(key, merchant, TTL);

        return merchant;
    }

    private String buildCacheKey(MerchantStatus status, Pageable pageable) {
        return String.format("merchants:status_%s:page_%d:size_%d:sort_%s",
                status != null ? status.name().toLowerCase() : "all",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }
}
