package com.group02.ev_maintenancesystem.configuration;

import com.group02.ev_maintenancesystem.dto.request.IntrospectRequest;
import com.group02.ev_maintenancesystem.service.JwtService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtDecoderConfig implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signKey;
    private NimbusJwtDecoder jwtDecoder;
    private final JwtService jwtService;
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = jwtService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());
            if (!response.isValid()) throw new JwtException("Token is invalid");

        } catch (ParseException | JOSEException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(jwtDecoder)) {
            SecretKey secretKey = new SecretKeySpec(signKey.getBytes(), "HS512");
            jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return jwtDecoder.decode(token);
    }
}
