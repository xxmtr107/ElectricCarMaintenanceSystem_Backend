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
@Table(name = "part_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Nhóm phụ tùng
 */
public class PartCategory extends BaseEntity{
    String name;
    String code;
    String description;

    //Relationships
    @OneToMany(mappedBy = "partCategory", fetch = FetchType.LAZY)
    List<SparePart> spareParts = new ArrayList<>();
}
