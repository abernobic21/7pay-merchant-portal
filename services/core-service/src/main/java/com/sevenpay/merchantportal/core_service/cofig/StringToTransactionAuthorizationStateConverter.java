package com.sevenpay.merchantportal.core_service.cofig;

import com.sevenpay.merchantportal.core_service.model.enums.TransactionAuthorizationState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTransactionAuthorizationStateConverter implements Converter<String, TransactionAuthorizationState> {

    @Override
    public TransactionAuthorizationState convert(String source) {
        return TransactionAuthorizationState.valueOf(source.toUpperCase());
    }
}
