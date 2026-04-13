package com.sevenpay.merchantportal.user_management_service.util;

import com.sevenpay.merchantportal.user_management_service.model.dto.UserDto;
import com.sevenpay.merchantportal.user_management_service.model.dto.UserProfileDto;
import com.sevenpay.merchantportal.user_management_service.model.entity.User;

public class UserMapper {
   private UserMapper(){}

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .accessLevel(user.getAccessLevel())
                .merchantId(user.getMerchantId())
                .status(user.getStatus())
                .firstLoginFlag(user.getFirstLoginFlag())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static UserProfileDto toUserProfileDto(User user) {
        return UserProfileDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .accessLevel(user.getAccessLevel())
                .merchantId(user.getMerchantId())
                .status(user.getStatus())
                .build();
    }
}
