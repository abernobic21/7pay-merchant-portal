package com.sevenpay.merchantportal.user_management_service.controller;

import com.sevenpay.merchantportal.user_management_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.user_management_service.model.dto.*;
import com.sevenpay.merchantportal.user_management_service.model.entity.User;
import com.sevenpay.merchantportal.user_management_service.model.enums.Role;
import com.sevenpay.merchantportal.user_management_service.model.enums.Status;
import com.sevenpay.merchantportal.user_management_service.service.EmailService;
import com.sevenpay.merchantportal.user_management_service.service.PasswordResetService;
import com.sevenpay.merchantportal.user_management_service.service.UserService;
import com.sevenpay.merchantportal.user_management_service.util.AuthGuard;
import com.sevenpay.merchantportal.user_management_service.util.UserMapper;
import jakarta.validation.Valid;
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
    private final AuthGuard authGuard;

    @GetMapping()
    public ResponseEntity<ApiResponse<UserDto>> getUsers(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Role role) {

        authGuard.requireAdmin(accessToken);
        return ResponseEntity.ok(userService.getUsers(pageable, status, role));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @RequestBody @Valid CreateUserRequest request) {

        authGuard.requireAdmin(accessToken);

        User user = userService.createUser(request);
        String token = passwordResetService.createSetPasswordToken(user);
        emailService.sendSetPasswordEmail(user.getEmail(), token);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PathVariable UUID id) {

        authGuard.requireAdmin(accessToken);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserRequest request) {

        authGuard.requireAdmin(accessToken);
        return ResponseEntity.ok().body(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PathVariable UUID id) {

        authGuard.requireAdmin(accessToken);
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDto> updateUserStatus(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserStatusRequest request) {

        authGuard.requireAdmin(accessToken);
        return ResponseEntity.ok(userService.updateUserStatus(id, request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @CookieValue(name = "access_token", required = false) String accessToken) {

        JwtTokenValidationResponse accessTokenData = authGuard.authenticate(accessToken);
        UUID userId = UUID.fromString(accessTokenData.getUserId());
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Map<String, String>> updatePassword(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @RequestBody @Valid UpdatePasswordRequest request) {

        JwtTokenValidationResponse accessTokenData = authGuard.authenticate(accessToken);
        UUID userId = UUID.fromString(accessTokenData.getUserId());
        userService.updatePassword(userId, request);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}
