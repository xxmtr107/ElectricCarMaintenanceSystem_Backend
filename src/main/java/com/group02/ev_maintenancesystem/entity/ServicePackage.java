package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Loại dịch vụ (Bảo dưỡng sơ cấp , Bảo dưỡng trung cấp , Bảo dưỡng cao cấp)
 */
public class ServicePackage extends BaseEntity {
    @Column(columnDefinition = "NVARCHAR(100)", nullable = false, unique = true)
    String name;

    @Column(columnDefinition = "NVARCHAR(200)")
    String description;

    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY)
    List<Appointment> appointments = new ArrayList<>();


}
