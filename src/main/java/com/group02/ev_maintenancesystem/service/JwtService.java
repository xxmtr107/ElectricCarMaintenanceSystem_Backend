package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean verifyToken(String token) throws ParseException, JOSEException;
}
