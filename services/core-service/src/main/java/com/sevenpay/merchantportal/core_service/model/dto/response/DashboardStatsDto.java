package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.dto.common.RevenueByAcquirerDto;
import com.sevenpay.merchantportal.core_service.model.dto.common.RevenueByCardBrandDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private BigDecimal totalRevenue;
    private Integer totalTransactions;
    private BigDecimal avgTransactionValue;
    private List<RevenueByCardBrandDto> revenueByCardBrand;
    private List<RevenueByAcquirerDto> revenueByAcquirer;
    private LocalDateTime cachedAt;
}
