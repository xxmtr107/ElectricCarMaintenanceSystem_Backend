package com.group02.ev_maintenancesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class EvMaintenanceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvMaintenanceSystemApplication.class, args);
    }

}
