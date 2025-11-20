package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.IntrospectRequest;
import com.group02.ev_maintenancesystem.dto.response.IntrospectResponse;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.InvalidatedRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.signerKey}")
    private String signerKey;
    private final InvalidatedRepository invalidatedRepository;
    @Override
    public String generateAccessToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expiredTime = Date.from(issueTime.toInstant().plus(1, ChronoUnit.HOURS));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", "ROLE_" + user.getRole().name())
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    @Override
    public String generateRefreshToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expiredTime = Date.from(issueTime.toInstant().plus(30, ChronoUnit.DAYS));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefreshToken) throws ParseException, JOSEException {
        var signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verify =  signedJWT.verify(new MACVerifier(signerKey.getBytes()));

        if (!(verify && expirationTime.after(new Date()))) {
            if(isRefreshToken) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        }
        if(invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        // Mặc định kiểm tra như là access token
        return verifyToken(token, false);
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token,false);

        } catch (AppException e) {
            // Nếu verify ném AppException (sai chữ ký, hết hạn VÀ LÀ REFRESH TOKEN, bị logout) -> không valid
            isValid = false;
        } catch (Exception e){
            // Các lỗi khác (như ParseException) cũng là không valid
            isValid = false;
            log.error("Error introspecting token", e); // Log lỗi khác
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public String generatePasswordResetToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        // Token reset chỉ có hiệu lực 15 PHÚT
        Date expiredTime = Date.from(issueTime.toInstant().plus(15, ChronoUnit.MINUTES));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // Lưu username để biết reset cho ai
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", "PASSWORD_RESET") // Đánh dấu đây là token reset
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.CANT_CREATE_NEW_PASSWORD);
        }
        return jwsObject.serialize();
    }


}
