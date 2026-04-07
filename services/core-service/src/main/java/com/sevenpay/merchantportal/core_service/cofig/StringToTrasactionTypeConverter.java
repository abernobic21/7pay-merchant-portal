package com.sevenpay.merchantportal.core_service.cofig;

import com.sevenpay.merchantportal.core_service.model.enums.TransactionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTrasactionTypeConverter implements Converter<String, TransactionType> {

    @Override
    public TransactionType convert(String source) {
        return TransactionType.valueOf(source.toUpperCase());
    }
}
