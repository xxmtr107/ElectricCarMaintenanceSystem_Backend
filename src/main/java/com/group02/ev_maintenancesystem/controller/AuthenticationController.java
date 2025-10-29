package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.IntrospectRequest;
import com.group02.ev_maintenancesystem.dto.request.LoginRequest;
import com.group02.ev_maintenancesystem.dto.request.LogoutRequest;
import com.group02.ev_maintenancesystem.dto.request.RefreshTokenRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.AuthenticationResponse;
import com.group02.ev_maintenancesystem.dto.response.IntrospectResponse;
import com.group02.ev_maintenancesystem.service.AuthenticationService;
import com.group02.ev_maintenancesystem.service.JwtService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    JwtService jwtService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login successful")
                .result(authenticationService.login(request))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .message("Token introspected successfully")
                .result(jwtService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .message("Logout successful")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Token refreshed successfully")
                .result(authenticationService.refreshToken(request.getRefreshToken()))
                .build();
    }

}
