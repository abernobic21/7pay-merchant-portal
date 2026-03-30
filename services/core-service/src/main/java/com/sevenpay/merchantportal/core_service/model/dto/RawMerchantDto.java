package com.sevenpay.merchantportal.core_service.model.dto;

import lombok.Data;

@Data
public class RawMerchantDto {
    private String shortName;
    private String fullName;
    private String oib;
    private String oibExtension;
    private String taxNumber;
    private AddressDto address;
    private String status;
}
