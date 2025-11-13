package com.group02.ev_maintenancesystem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing
 * This allows automatic population of @CreatedBy, @CreatedDate, @LastModifiedBy, @LastModifiedDate
 */

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@DependsOn("flyway")
public class JpaConfig {
    // Empty configuration class
    // Just enables JPA Auditing with custom AuditorAware
}