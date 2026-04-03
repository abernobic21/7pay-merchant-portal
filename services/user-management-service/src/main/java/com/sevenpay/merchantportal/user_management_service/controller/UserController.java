package com.sevenpay.merchantportal.user_management_service.controller;

import com.sevenpay.merchantportal.user_management_service.model.common.ApiResponse;
import com.sevenpay.merchantportal.user_management_service.model.dto.UserDto;
import com.sevenpay.merchantportal.user_management_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
        System.out.println("Controller");
        return ResponseEntity.ok(userService.getUsers(page, size));
    }
}
