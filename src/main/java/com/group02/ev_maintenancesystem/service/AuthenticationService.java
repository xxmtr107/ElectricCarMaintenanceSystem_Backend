package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.LoginRequest;
import com.group02.ev_maintenancesystem.dto.request.LogoutRequest;
import com.group02.ev_maintenancesystem.dto.response.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest request);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
}
