package com.group02.ev_maintenancesystem.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                    new Server().url("https://electriccarmaintenancesystembackend-production.up.railway.app/api").description("Production"),
                    new Server().url("http://localhost:8080/api").description("Local")))
                .info(new Info()
                        .title("Electric Car Maintenance System API")
                        .version("1.0.0")
                        .description("API documentation for Electric Car Maintenance System")
                        .contact(new Contact()
                                .name("Thu Thao")
                                .email("thaontse184164@for.edu.vn")
                                .url("https://electric-car-maintenance.vercel.app/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                // Thêm Bearer Token Authentication
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")));
    }

}
