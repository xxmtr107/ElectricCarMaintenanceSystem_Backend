package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.IntrospectRequest;
import com.group02.ev_maintenancesystem.dto.response.IntrospectResponse;
import com.group02.ev_maintenancesystem.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    SignedJWT verifyToken(String token) throws ParseException, JOSEException;
    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;
}
