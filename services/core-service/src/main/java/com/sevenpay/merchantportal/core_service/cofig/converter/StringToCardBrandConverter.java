package com.sevenpay.merchantportal.core_service.cofig.converter;

import com.sevenpay.merchantportal.core_service.model.enums.CardBrand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCardBrandConverter implements Converter<String, CardBrand> {

    @Override
    public CardBrand convert(String source) {
        return CardBrand.valueOf(source.toUpperCase());
    }
}
