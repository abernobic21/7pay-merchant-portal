package com.sevenpay.merchantportal.core_service.model.dto.common;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RevenueByCardBrandDto {
    private String cardBrand;
    private BigDecimal revenue;
    private Integer count;
}
