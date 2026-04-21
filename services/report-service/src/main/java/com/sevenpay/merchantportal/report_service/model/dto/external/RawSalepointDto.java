package com.sevenpay.merchantportal.report_service.model.dto.external;

import com.sevenpay.merchantportal.report_service.model.dto.common.AcquirerMerchantDataDto;
import com.sevenpay.merchantportal.report_service.model.dto.common.AddressDto;
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
