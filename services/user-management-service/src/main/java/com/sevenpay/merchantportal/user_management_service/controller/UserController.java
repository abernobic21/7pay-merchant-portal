package com.sevenpay.merchantportal.user_management_service.controller;

import com.sevenpay.merchantportal.user_management_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.user_management_service.model.dto.CreateUserRequest;
import com.sevenpay.merchantportal.user_management_service.model.dto.UpdateUserRequest;
import com.sevenpay.merchantportal.user_management_service.model.dto.UpdateUserStatusRequest;
import com.sevenpay.merchantportal.user_management_service.model.dto.UserDto;
import com.sevenpay.merchantportal.user_management_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    @GetMapping()
    public ResponseEntity<ApiResponse<UserDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getUsers(page, size));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request){
        return ResponseEntity.ok().body(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteUser(
            @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message","User deleted successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable UUID id, @RequestBody @NotNull UpdateUserStatusRequest request){
        return ResponseEntity.ok(userService.updateUserStatus(id, request));
    }
}
