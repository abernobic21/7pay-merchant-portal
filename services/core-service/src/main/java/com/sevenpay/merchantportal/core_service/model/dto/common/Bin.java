package com.sevenpay.merchantportal.core_service.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sevenpay.merchantportal.core_service.model.enums.BINType;
import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bin {

    @JsonProperty("startBIN")
    private int startBin;

    @JsonProperty("endBIN")
    private int endBin;

    private int length;
    private CardBrand cardBrand;

    @JsonProperty("assignedHost")
    private int assignedHost;

    private BINType BINType;
}

