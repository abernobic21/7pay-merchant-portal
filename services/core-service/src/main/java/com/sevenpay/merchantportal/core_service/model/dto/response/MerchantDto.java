package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.dto.common.AddressDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantDto {
    private String shortName;
    private String fullName;
    private String oib;
    private String status;
    private AddressDto address;
}
