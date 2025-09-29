package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Loại dịch vụ (Bảo dưỡng sơ cấp , Bảo dưỡng trung cấp , Bảo dưỡng cao cấp)
 */
public class ServiceType extends BaseEntity {
    String name;
    String description;


    //Relationships
    @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
    List<ServiceItem> serviceItems = new ArrayList<>();

    @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
    List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
}
