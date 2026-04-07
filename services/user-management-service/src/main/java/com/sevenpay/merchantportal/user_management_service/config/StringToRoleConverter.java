package com.sevenpay.merchantportal.user_management_service.config;

import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        return Role.valueOf(source.toUpperCase());
    }
}
