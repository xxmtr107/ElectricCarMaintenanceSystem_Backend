package com.group02.ev_maintenancesystem;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class EvMaintenanceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvMaintenanceSystemApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Thiết lập múi giờ mặc định là Asia/Ho_Chi_Minh (GMT+7)
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }
}
