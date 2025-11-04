package com.group02.ev_maintenancesystem.configuration;

import com.group02.ev_maintenancesystem.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", // Swagger
//                                "/auth/**"              // Login, Refresh, Logout, Introspect
//                        ).permitAll()
//                        // Allow GET all vehicle models and service centers
//                        .requestMatchers(HttpMethod.GET, "/vehicleModel", "/service-centers", "/service-centers/search", "/service-centers/{id}").permitAll()
//
//                        // --- 2. Endpoint ADMIN (Quản lý hệ thống) ---
//                        .requestMatchers(
//                                "/staffs/**",              // Quản lý Staff
//                                "/vehicleModel/**",        // Quản lý Model xe
//                                "/servicePackage/**",      // Quản lý Gói
//                                "/serviceItem/**",         // Quản lý Hạng mục
//                                "/model-package-items/**", // Quản lý Menu giá
//                                "/service-centers/**"      // Quản lý Service Center (POST, PUT, DELETE)
//                        ).hasRole("ADMIN") // Chỉ ADMIN
//
//                        // --- 3. Endpoint STAFF & ADMIN (Nghiệp vụ) ---
//                        .requestMatchers(
//                                "/appointments", // GET all
//                                "/appointments/status/**",
//                                "/appointments/date-range",
//                                "/appointments/{appointmentId}/assign/{technicianId}",
//                                "/maintenance-records/**",
//                                "/vehicles", // GET all vehicles
//                                "/technicians/**"      // Quản lý Technician
//                        ).hasAnyRole("ADMIN", "STAFF")
//
//                        // --- 4. Endpoint TECHNICIAN (và cao hơn) ---
//                        .requestMatchers(
//                                "/appointments/setStatus/**",
//                                "/appointments/technician/**"
//
//                        ).hasAnyRole("ADMIN", "STAFF", "TECHNICIAN")
//
//                        .requestMatchers(HttpMethod.POST, "/maintenance-records/{recordId}/parts")
//                        .hasAnyRole("TECHNICIAN")
//                        // --- 5. Các endpoint còn lại ---
//                        // Yêu cầu phải đăng nhập (Authenticated)
//                        // Quyền chi tiết (như CUSTOMER có đúng là chủ xe không)
//                        // sẽ được xử lý ở Service hoặc @PreAuthorize
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoderConfig))
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("scope");

        jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(Arrays.asList(
//                "http://localhost:3000",
//                "http://localhost:5173",
//                "https://electric-car-maintenance.vercel.app",
//                "https://reliable-rebirth-production.up.railway.app",  // Thêm domain Railway
//                "https://electriccarmaintenancesystem-production.up.railway.app"  // Domain từ VNPay config
//        ));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
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

}