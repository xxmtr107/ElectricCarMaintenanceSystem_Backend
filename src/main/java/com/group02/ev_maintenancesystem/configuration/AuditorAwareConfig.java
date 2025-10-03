package com.group02.ev_maintenancesystem.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
@Slf4j
public class AuditorAwareConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            log.debug("No authenticated user found, returning 'system' as auditor");
            return Optional.of("SYSTEM");
        }

        String username = authentication.getName();
        log.debug("Current auditor: {}", username);

        return Optional.of(username);
    }
}