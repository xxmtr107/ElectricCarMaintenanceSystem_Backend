package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity{
    @Column(nullable = false, unique = true)
    String name;

    String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    List<User> user = new ArrayList<>();



}

