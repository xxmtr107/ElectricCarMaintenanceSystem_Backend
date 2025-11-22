package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

/**
 * Chi tiết dịch vụ
 */
public class ServiceItem extends BaseEntity {

    @Column(columnDefinition = "NVARCHAR(100)", nullable = false, unique = true)
    String name;

    @JsonIgnore
    @Column(columnDefinition = "NVARCHAR(200)")
    String description;

    @JsonIgnore
    @OneToMany(mappedBy = "serviceItem", fetch = FetchType.LAZY)
    List<ModelPackageItem> modelPackageItems = new ArrayList<>();




}
