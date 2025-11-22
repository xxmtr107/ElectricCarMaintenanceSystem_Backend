package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group02.ev_maintenancesystem.enums.Gender;
import com.group02.ev_maintenancesystem.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.jdbc.Work;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table (name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "UK_username", columnNames = "username"),
        @UniqueConstraint(name = "UK_email", columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {


    // ========== AUTHENTICATION FIELDS ==========
    @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String phone;

    @Column(name = "last_login")
    LocalDateTime lastLogin;

    // ========== PROFILE FIELDS (Common for all users) ==========
    @Column(name = "full_name", nullable = false)
    String fullName;

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    Gender gender;

    // Relationships
    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "customerUser")
    List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "customerUser")
    List<Appointment> customerAppointments = new ArrayList<>();

    @OneToMany(mappedBy = "technicianUser")
    List<Appointment> technicianAppointments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "center_id")
    ServiceCenter serviceCenter;

    public boolean isCustomer() {
        return role == Role.CUSTOMER; // CẬP NHẬT
    }

    public boolean isTechnician() {
        return role == Role.TECHNICIAN; // CẬP NHẬT
    }

    public boolean isAdmin() {
        return role == Role.ADMIN; // CẬP NHẬT
    }

    public boolean isStaff() {
        return role == Role.STAFF; // CẬP NHẬT
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}