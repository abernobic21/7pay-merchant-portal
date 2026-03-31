package com.sevenpay.merchantportal.core_service.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RawSalepointDto {

    private String name;
    private String mid;
    private AddressDto address;
    private String status;
    private String merchantOib;
    private List<AcquirerMerchantDataDto> acquirerMerchantData;
}
