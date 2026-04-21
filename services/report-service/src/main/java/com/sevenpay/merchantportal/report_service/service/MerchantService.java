package com.sevenpay.merchantportal.report_service.service;

import com.sevenpay.merchantportal.report_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawMerchantDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantApiClient merchantApiClient;

    public Map<String, RawMerchantDetailDto> getMerchants(List<String> oibs) {

        return oibs.stream()
                .collect(Collectors.toMap(
                        oib -> oib,
                        merchantApiClient::fetchMerchantByOib
                ));
    }
}

