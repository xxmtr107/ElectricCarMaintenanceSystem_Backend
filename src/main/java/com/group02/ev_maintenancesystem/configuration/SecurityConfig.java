package com.group02.ev_maintenancesystem.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.signerKey}")
    private String SIGN_KEY;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // tất cả request đều được phép
                )
                .oauth2ResourceServer(oauth2 -> oauth2.disable()); // tắt oauth2 resource server

        return http.build();
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        // BCrypt với strength 10 (cân bằng giữa security và performance)
        // Strength càng cao thì càng secure nhưng càng chậm
        return new BCryptPasswordEncoder(10);
    }

}
