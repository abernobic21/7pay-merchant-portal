package com.sevenpay.merchantportal.user_management_service.service;

import com.sevenpay.merchantportal.user_management_service.exception.ConflictException;
import com.sevenpay.merchantportal.user_management_service.exception.NotFoundException;
import com.sevenpay.merchantportal.user_management_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.user_management_service.model.common.PageInfo;
import com.sevenpay.merchantportal.user_management_service.model.dto.CreateUserRequest;
import com.sevenpay.merchantportal.user_management_service.model.dto.UserDto;
import com.sevenpay.merchantportal.user_management_service.model.entity.User;
import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import com.sevenpay.merchantportal.user_management_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ApiResponse<UserDto> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDto> userDtos = userPage.getContent()
                .stream()
                .map(this::toUserDto)
                .toList();

        return buildResponse(userDtos, userPage);
    }

    public UserDto getUserById(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException());

        return toUserDto(user);
    }

    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(null)
                .role(request.getRole())
                .accessLevel(request.getAccessLevel())
                .merchantId(request.getMerchantId())
                .status(Status.ACTIVE)
                .firstLoginFlag(true)
                .build();

        userRepository.save(user);
    }

    private UserDto toUserDto(User user) {
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

    private <T> ApiResponse<T> buildResponse(List<T> content, org.springframework.data.domain.Page<?> userPage) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalPages(userPage.getTotalPages());
        pageInfo.setTotalElements((int) userPage.getTotalElements());
        pageInfo.setSize(userPage.getSize());
        pageInfo.setNumber(userPage.getNumber());

        ApiResponse<T> response = new ApiResponse<>();
        response.setContent(content);
        response.setPage(pageInfo);

        return response;
    }
}
