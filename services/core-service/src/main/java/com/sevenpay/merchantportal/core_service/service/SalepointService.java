package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawSalepointDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.SalepointDto;
import com.sevenpay.merchantportal.core_service.util.mapper.SalepointMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalepointService {

    private final MerchantApiClient merchantApiClient;
    private final RedisCacheService cacheService;
    private static final Duration TTL = Duration.ofMinutes(15);

    public ApiResponse<SalepointDto> getSalepoints(String mid, Pageable pageable) {
        String key = buildCacheKey(mid, pageable);

        ApiResponse<SalepointDto> cached = cacheService.get(key);
        if (cached != null) {
            return cached;
        }
        ApiResponse<RawSalepointDto> rawResponse = mid != null
                ? merchantApiClient.fetchSalepointsByMerchantOib(mid, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort())
                : merchantApiClient.fetchSalepoints(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<SalepointDto> salepoints = rawResponse.getContent()
                .stream()
                .map(SalepointMapper::toSalepointDto)
                .collect(Collectors.toList());

        ApiResponse<SalepointDto> response = new ApiResponse<>();
        response.setContent(salepoints);
        response.setPage(rawResponse.getPage());

        cacheService.set(key, response, TTL);
        return response;
    }

    public SalepointDetailDto getSalepointByMid(String mid) {
        String key = "salepoint:" + mid;

        SalepointDetailDto cached = cacheService.get(key);
        if(cached != null){
            return cached;
        }

        RawSalepointDto rawSalepoint = merchantApiClient.fetchSalepointByMid(mid);
        SalepointDetailDto salepoint = SalepointMapper.toSalepointDetailDto(rawSalepoint);
        cacheService.set(key, salepoint, TTL);

        return salepoint;
    }

    private String buildCacheKey(String mid, Pageable pageable) {
        return String.format("salepoints:mid_%s:page_%d:size_%d:sort_%s",
                mid != null ? mid.toLowerCase() : "all",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }
}
