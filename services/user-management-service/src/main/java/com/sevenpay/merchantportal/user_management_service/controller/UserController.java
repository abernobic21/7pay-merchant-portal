package com.sevenpay.merchantportal.user_management_service.controller;

import com.sevenpay.merchantportal.user_management_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.user_management_service.model.dto.*;
import com.sevenpay.merchantportal.user_management_service.model.entity.User;
import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import com.sevenpay.merchantportal.user_management_service.service.EmailService;
import com.sevenpay.merchantportal.user_management_service.service.PasswordResetService;
import com.sevenpay.merchantportal.user_management_service.service.UserService;
import com.sevenpay.merchantportal.user_management_service.util.UserMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;

    @GetMapping()
    public ResponseEntity<ApiResponse<UserDto>> getUsers(
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Role role
    ){
        return ResponseEntity.ok(userService.getUsers(pageable, status, role));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserRequest request) {

        User user = userService.createUser(request);
        String token = passwordResetService.createSetPasswordToken(user);
        emailService.sendSetPasswordEmail(user.getEmail(), token);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok().body(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable UUID id, @RequestBody @NotNull UpdateUserStatusRequest request) {
        return ResponseEntity.ok(userService.updateUserStatus(id, request));
    }


    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        UUID hardcodedId = UUID.fromString("db0c535d-aee8-4ca9-bb13-052795c04e07");
        return ResponseEntity.ok(userService.getUserProfile(hardcodedId));
    }
}
