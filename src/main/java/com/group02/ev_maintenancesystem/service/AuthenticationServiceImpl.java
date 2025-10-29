package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.LoginRequest;
import com.group02.ev_maintenancesystem.dto.request.LogoutRequest;
import com.group02.ev_maintenancesystem.dto.response.AuthenticationResponse;
import com.group02.ev_maintenancesystem.entity.InvalidatedToken;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.InvalidatedRepository;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    AuthenticationManager authenticationManager;
    JwtService jwtService;
    InvalidatedRepository InvalidatedRepository;
    UserRepository userRepository;

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try{
            var signToken = jwtService.verifyToken(request.getToken(),false);
            String jwtId = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidToken = InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();

            InvalidatedRepository.save(invalidToken);
        } catch (AppException e){
            log.info("Token is already invalidated");
        }
    }
    public AuthenticationResponse refreshToken(String refreshToken) throws ParseException, JOSEException {
        var signedJWT = jwtService.verifyToken(refreshToken, true);
        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidToken = InvalidatedToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();
        InvalidatedRepository.save(invalidToken);
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .authenticated(true)
                .build();
    }
}
