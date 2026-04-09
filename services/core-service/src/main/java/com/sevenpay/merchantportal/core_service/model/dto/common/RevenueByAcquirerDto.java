package com.sevenpay.merchantportal.core_service.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class RevenueByAcquirerDto {
    private String acquirerHost;
    private BigDecimal revenue;
    private Integer count;
}
