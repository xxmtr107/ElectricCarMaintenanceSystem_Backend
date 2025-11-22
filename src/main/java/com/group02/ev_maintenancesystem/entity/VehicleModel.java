package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
@SQLDelete(sql = "UPDATE vehicle_models SET record_status = 'DELETED' WHERE id = ?")
@SQLRestriction("record_status <> 'DELETED'")
public class VehicleModel extends  BaseEntity {
    @Column(nullable = false, unique = true)
    String name;

    @Column(name = "model_year")
    String modelYear;

    @OneToMany (mappedBy = "model", fetch = FetchType.LAZY)
    List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "vehicleModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ModelPackageItem> modelPackageItems = new ArrayList<>();

}
