package com.sevenpay.merchantportal.user_management_service.config;

import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusConverter implements Converter<String, Status> {

    @Override
    public Status convert(String source) {
        return Status.valueOf(source.toUpperCase());
    }
}
