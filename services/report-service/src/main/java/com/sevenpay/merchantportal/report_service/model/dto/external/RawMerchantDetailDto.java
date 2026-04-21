package com.sevenpay.merchantportal.report_service.model.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sevenpay.merchantportal.report_service.model.dto.common.AddressDto;
import com.sevenpay.merchantportal.report_service.model.dto.common.Bin;
import com.sevenpay.merchantportal.report_service.model.enums.CardBrand;
import com.sevenpay.merchantportal.report_service.model.enums.TransactionType;
import lombok.Data;

import java.util.List;

@Data
public class RawMerchantDetailDto {
    private String shortName;
    private String fullName;
    private String oib;
    private String oibExtension;
    private String taxNumber;
    private AddressDto address;
    private String status;
    @JsonProperty("prefixTID")
    private String prefixTid;
    @JsonProperty("prefixMID")
    private String prefixMid;
    private List<TransactionType> supportedTransactionTypes;
    private List<String> supportedAcquirers;
    private List<CardBrand> supportedCardBrands;
    @JsonProperty("supportedBINs")
    private List<Bin> supportedBins;
}
