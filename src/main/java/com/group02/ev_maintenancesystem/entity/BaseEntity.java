package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "create_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime createdAt;

    @JsonIgnore
    @LastModifiedDate // Automatically set the last modified date
    @Column(name = "update_at", columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @CreatedBy // Automatically set the user who created the entity
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @JsonIgnore
    @LastModifiedBy // Automatically set the user who last modified the entity
    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist // Method to set createdAt before persisting (INSERT)
    protected void onCreate(){
        if(this.createdAt == null){
            this.createdAt = LocalDateTime.now().withNano(0);
        }
    }

    @PreUpdate // Method to set updatedAt before updating (UPDATE)
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now().withNano(0);
    }
}
