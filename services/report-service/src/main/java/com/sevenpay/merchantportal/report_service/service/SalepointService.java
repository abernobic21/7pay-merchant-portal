package com.sevenpay.merchantportal.report_service.service;

import com.sevenpay.merchantportal.report_service.client.MerchantApiClient;
import com.sevenpay.merchantportal.report_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.report_service.model.dto.external.RawSalepointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalepointService {

    private final MerchantApiClient merchantApiClient;

    public Map<String, RawSalepointDto> getSalepoints(List<String> oibs, List<String> mids) {
        List<RawSalepointDto> allSalepoints = new ArrayList<>();

        for (String oib : oibs) {
            allSalepoints.addAll(fetchAllSalepointsForOib(oib));
        }

        return allSalepoints.stream()
                .filter(sp -> mids.isEmpty() || mids.contains(sp.getMid()))
                .collect(Collectors.toMap(
                        RawSalepointDto::getMid,
                        sp -> sp
                ));
    }

    private List<RawSalepointDto> fetchAllSalepointsForOib(String oib) {

        ApiResponse<RawSalepointDto> firstPage = merchantApiClient
                .fetchSalepointsByMerchantOib(oib, 0, 100, Sort.unsorted());

        List<RawSalepointDto> result = new ArrayList<>(firstPage.getContent());

        int totalPages = firstPage.getPage().getTotalPages();

        for (int page = 1; page < totalPages; page++) {
            ApiResponse<RawSalepointDto> nextPage = merchantApiClient
                    .fetchSalepointsByMerchantOib(oib, page, 100, Sort.unsorted());
            result.addAll(nextPage.getContent());
        }

        return result;
    }
}
