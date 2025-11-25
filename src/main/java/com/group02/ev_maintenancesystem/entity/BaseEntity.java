package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group02.ev_maintenancesystem.enums.GeneralStatus;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@MappedSuperclass // Indicates that this class is a base class for other entities
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @CreatedDate // Automatically set the creation date
    @Column(name = "create_at", updatable = false, columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime createdAt;

    @JsonIgnore
    @LastModifiedDate // Automatically set the last modified date
    @Column(name = "update_at", columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @CreatedBy // Automatically set the user who created the entity
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    // --- THAY ĐỔI Ở ĐÂY ---
    // Mặc định là TRUE (Hoạt động) khi tạo mới
    @Column(name = "is_active", nullable = false, columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean active = true;

    @JsonIgnore
    @LastModifiedBy // Automatically set the user who last modified the entity
    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate(){
        if(this.createdAt == null){
            // Ép kiểu về múi giờ Việt Nam
            this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withNano(0);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Ép kiểu về múi giờ Việt Nam
        this.updatedAt = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withNano(0);
    }
}
