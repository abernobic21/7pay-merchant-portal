package com.sevenpay.merchantportal.core_service.service;

import com.sevenpay.merchantportal.core_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.core_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.core_service.model.dto.external.RawTerminalDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDto;
import com.sevenpay.merchantportal.core_service.model.enums.TerminalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerminalService {

    private final MerchantApiClient merchantApiClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofMinutes(15);

    public ApiResponse<TerminalDto> getTerminals(String merchantOib, String mid, Pageable pageable) {
        String key = buildCacheKey(merchantOib, mid, pageable);

        ApiResponse<TerminalDto> cached = (ApiResponse<TerminalDto>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

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

        redisTemplate.opsForValue().set(key, response, TTL);
        return response;
    }

    public TerminalDetailDto getTerminalByTid(String tid) {
        String key = "terminal:" + tid;

        TerminalDetailDto cached = (TerminalDetailDto) redisTemplate.opsForValue().get(key);
        if(cached != null){
            return cached;
        }

        RawTerminalDto rawTerminal = merchantApiClient.fetchTerminalByTid(tid);

        TerminalDetailDto terminal = toTerminalDetailDto(rawTerminal);
        redisTemplate.opsForValue().set(key, terminal, TTL);

        return terminal;
    }

    private String buildCacheKey(String merchantOib, String mid, Pageable pageable) {
        return String.format("terminals:merchantOib_%s:mid_%s:page_%d:size_%d:sort_%s",
                merchantOib != null ? merchantOib.toLowerCase() : "all",
                mid != null ? mid.toLowerCase() : "all",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
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

    private TerminalDetailDto toTerminalDetailDto(RawTerminalDto rawTerminalDto) {
        return TerminalDetailDto.builder()
                .tid(rawTerminalDto.getTid())
                .terminalType(rawTerminalDto.getTerminalType())
                .status(TerminalStatus.getByName(rawTerminalDto.getStatus()))
                .midReference(rawTerminalDto.getMidReference())
                .phoneNumber(rawTerminalDto.getPhoneNumber())
                .emailContact(rawTerminalDto.getEmailContact())
                .currency(rawTerminalDto.getCurrency())
                .merchantCategoryCode(rawTerminalDto.getMerchantCategoryCode())
                .authenticationMode(rawTerminalDto.getAuthenticationMode())
                .softPosId(rawTerminalDto.getSoftPosId())
                .softPosStatus(rawTerminalDto.getSoftPosStatus())
                .acquirerTerminalData(rawTerminalDto.getAcquirerTerminalData())
                .tipEnabled(rawTerminalDto.getTipEnabled())
                .build();
    }
}


