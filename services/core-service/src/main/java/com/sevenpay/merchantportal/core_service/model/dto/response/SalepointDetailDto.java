package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.dto.common.AcquirerMerchantDataDto;
import com.sevenpay.merchantportal.core_service.model.dto.common.AddressDto;
import com.sevenpay.merchantportal.core_service.model.enums.SalepointStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SalepointDetailDto {

    private String name;
    private String mid;
    private AddressDto address;
    private SalepointStatus status;
    private String merchantOib;
    private List<AcquirerMerchantDataDto> acquirerMerchantData;
}
