package com.sevenpay.merchantportal.user_management_service.service;

import com.sevenpay.merchantportal.user_management_service.exception.ConflictException;
import com.sevenpay.merchantportal.user_management_service.exception.NotFoundException;
import com.sevenpay.merchantportal.user_management_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.user_management_service.model.common.PageInfo;
import com.sevenpay.merchantportal.user_management_service.model.dto.*;
import com.sevenpay.merchantportal.user_management_service.model.entity.User;
import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import com.sevenpay.merchantportal.user_management_service.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ApiResponse<UserDto> getUsers(Pageable pageable, Status status, Role role){
        Page<User> userPage = userRepository.findAll(buildSpec(status, role), pageable);

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

    public UserDto createUser(CreateUserRequest request) {
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

        return toUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getAccessLevel() != null) user.setAccessLevel(request.getAccessLevel());
        if (request.getMerchantId() != null) user.setMerchantId(request.getMerchantId());

        return toUserDto(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        userRepository.delete(user);
    }

    public UserDto updateUserStatus(UUID id, UpdateUserStatusRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        user.setStatus(request.getStatus());
        return toUserDto(userRepository.save(user));
    }

    public UserProfileDto getUserProfile(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException());

        return toUserProfileDto(user);
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

    private UserProfileDto toUserProfileDto(User user) {
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

    private Specification<User> buildSpec(Status status, Role role) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (role != null) predicates.add(cb.equal(root.get("role"), role));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private <T> ApiResponse<T> buildResponse(List<T> content, Page<?> userPage) {
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
