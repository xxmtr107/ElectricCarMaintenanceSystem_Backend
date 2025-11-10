package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group02.ev_maintenancesystem.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.jdbc.Work;
import org.springframework.security.core.GrantedAuthority;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @OneToMany(mappedBy = "customerUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "customerUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Appointment> customerAppointments = new ArrayList<>();

    @OneToMany(mappedBy = "technicianUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Appointment> technicianAppointments = new ArrayList<>();

    @OneToMany(mappedBy = "createdByAdmin", fetch = FetchType.LAZY) // ADMIN
    List<WorkSchedule> createdWorkSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY) // STAFF or TECHNICIAN
    List<WorkSchedule> workSchedules = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "center_id")
    ServiceCenter serviceCenter;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<ChatRoom> chatRooms = new HashSet<>();

    public boolean isCustomer() {
        return role != null && "CUSTOMER".equals(role.getName());
    }

    public boolean isTechnician() {
        return role != null && "TECHNICIAN".equals(role.getName());
    }

    public boolean isAdmin() {
        return role != null && "ADMIN".equals(role.getName());
    }

    public boolean isStaff() {
        return role != null && "STAFF".equals(role.getName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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