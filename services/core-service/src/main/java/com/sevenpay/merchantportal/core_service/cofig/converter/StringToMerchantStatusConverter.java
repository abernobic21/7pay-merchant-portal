package com.sevenpay.merchantportal.core_service.cofig.converter;

import com.sevenpay.merchantportal.core_service.model.enums.MerchantStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMerchantStatusConverter implements Converter<String, MerchantStatus> {

    @Override
    public MerchantStatus convert(String source) {
        return MerchantStatus.valueOf(source.toUpperCase());
    }
}
