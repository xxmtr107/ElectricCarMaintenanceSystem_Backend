package com.group02.ev_maintenancesystem.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()

                ).build();
//                .userDetailsService(authenticationService)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(secufilter, UsernamePasswordAuthenticationFilter.class).build();
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        // BCrypt với strength 10 (cân bằng giữa security và performance)
        // Strength càng cao thì càng secure nhưng càng chậm
        return new BCryptPasswordEncoder(10);
    }



}
