package com.group02.ev_maintenancesystem.controller;


import com.group02.ev_maintenancesystem.dto.request.UserCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;
    private final RestClient.Builder builder;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
//        var Authenthication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Username: {}", Authenthication.getName());
//        Authenthication.getAuthorities().forEach(grantedAuthority ->
//                log.info("Authority: {}", grantedAuthority));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id){


        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByID(id))
                .build();
    }

//    @GetMapping("/myInfo")
//    public ApiResponse<UserResponse> getMyInfo(){
//        return ApiResponse.<UserResponse>builder()
//                .result(userService.getMyInfo())
//                .build();
//    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ApiResponse.<String>builder()
                .result("User deleted successfully")
                .build();
    }
}
