package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawMerchantDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.MerchantDto;
import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantApiClient merchantApiClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofMinutes(15);

    public ApiResponse<MerchantDto> getMerchants(MerchantStatus status, Pageable pageable) {
        String key = buildCacheKey(status, pageable);

        ApiResponse<MerchantDto> cached = (ApiResponse<MerchantDto>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        ApiResponse<RawMerchantDto> rawResponse = status != null
                ? merchantApiClient.fetchMerchantsByStatus(status, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort())
                : merchantApiClient.fetchMerchants(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<MerchantDto> merchants = rawResponse.getContent()
                .stream()
                .map(this::toMerchantDto)
                .collect(Collectors.toList());

        ApiResponse<MerchantDto> response = new ApiResponse<>();
        response.setContent(merchants);
        response.setPage(rawResponse.getPage());

        redisTemplate.opsForValue().set(key, response, TTL);
        return response;
    }

    public MerchantDetailDto getMerchantByOib(String oib) {
        String key = "merchant:" + oib;

        MerchantDetailDto cached = (MerchantDetailDto) redisTemplate.opsForValue().get(key);
        if(cached != null){
            return cached;
        }

        RawMerchantDetailDto rawMerchant = merchantApiClient.fetchMerchantByOib(oib);

        MerchantDetailDto merchant = toMerchantDetailDto(rawMerchant);
        redisTemplate.opsForValue().set(key, merchant, TTL);

        return merchant;
    }

    private String buildCacheKey(MerchantStatus status, Pageable pageable) {
        return String.format("merchants:status_%s:page_%d:size_%d",
                status != null ? status.name().toLowerCase() : "all",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
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
