package com.group02.ev_maintenancesystem.entity;

import com.group02.ev_maintenancesystem.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends BaseEntity{

    String fullname;

    LocalDate dob;

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    Gender gender;

    String address;

    // Relationships
    @OneToOne(cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Appointment> appointments = new ArrayList<>();

}
