package com.group02.ev_maintenancesystem.configuration;

import com.group02.ev_maintenancesystem.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${jwt.signerKey}")
    private String signKey;

    private final CustomUserDetailService userDetailsService;
    private final JwtDecoderConfig jwtDecoderConfig;
    //Của triết

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // tất cả request đều được phép
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoderConfig))); // Cấu hình JWT


        return http.build();
    }
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//            .csrf(AbstractHttpConfigurer::disable)
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .authorizeHttpRequests(auth -> auth
//                    // Cho phép Swagger
//                    .requestMatchers(
//                            "/swagger-ui/**",
//                            "/v3/api-docs/**",
//                            "/swagger-ui.html"
//                    ).permitAll()
//
//                    // Cho phép VNPay callback (return & ipn)
//                    .requestMatchers("/vnpay/**").permitAll()
//
//                    // Cho phép đăng ký / login
//                    .requestMatchers("/auth/**").permitAll()
//
//                    // Còn lại cần JWT
//                    .anyRequest().authenticated()
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2
//                    .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoderConfig))
//            );
//
//    return http.build();
//}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"
//                "http://localhost:3000",
//                "http://localhost:5173",
//                "https://electric-car-maintenance.vercel.app",
//                "https://reliable-rebirth-production.up.railway.app",  // Thêm domain Railway
//                "https://electriccarmaintenancesystem-production.up.railway.app"  // Domain từ VNPay config
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        // BCrypt với strength 10 (cân bằng giữa security và performance)
        // Strength càng cao thì càng secure nhưng càng chậm
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }


    //Config CORS // của triết
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }


}
