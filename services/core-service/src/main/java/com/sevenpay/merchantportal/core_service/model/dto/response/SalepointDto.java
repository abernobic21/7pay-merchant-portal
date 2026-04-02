package com.sevenpay.merchantportal.core_service.model.dto.response;

import com.sevenpay.merchantportal.core_service.model.dto.common.AddressDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalepointDto {

    private String name;
    private String mid;
    private AddressDto address;
    private String status;
    private String merchantOib;
}
