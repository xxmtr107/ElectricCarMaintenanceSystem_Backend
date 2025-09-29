package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicle_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleModel extends  BaseEntity {
    String name;

    @Column(name = "model_year")
    String modelYear;

    @Column(name = "basic_warranty", precision = 5, scale = 2)
    Integer basicWarranty = 10000; // Bảo hành cơ bản (km)

    @Column(name = "comprehensive_warranty", precision = 6, scale = 2)
    Integer comprehensiveWarranty = 2000; // Bảo hành toàn diện (km)

    @Column(name = "basic_warranty_time")
    Integer basicWarrantyTime = 6; // Bảo hành cơ bản (tháng)

    @Column(name = "comprehensive_warranty_time")
    Integer comprehensiveWarrantyTime = 12; // Bảo hành toàn diện (tháng)

    @OneToMany (mappedBy = "model", fetch = FetchType.LAZY)
    List<Vehicle> vehicles = new ArrayList<>();

}
