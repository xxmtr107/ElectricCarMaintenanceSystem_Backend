package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spare_parts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Danh mục phụ tùng cụ thể
 */
public class SparePart extends  BaseEntity {
    @Column(name = "part_number", nullable = false, unique = true)
    String partNumber;

    String name;

    @Column(name = "unit_price", nullable = false,precision = 10, scale = 2)
    BigDecimal unitPrice;

    @Column(name = "quantity_in_stock", nullable = false)
    Integer quantityInStock; // Số lượng trong kho

    @Column(name = "minimum_stock_level", nullable = false, columnDefinition = "int default 10") // Mức tồn kho tối thiểu, không null, giá trị mặc định là 10
    Integer minimumStockLevel;

    @Column(name = "category_name")
    String categoryName;

    @Column(name = "category_code")
    String categoryCode;
    // Relationships
    @OneToMany(mappedBy = "sparePart", fetch = FetchType.LAZY)
    List<PartUsage> partUsages = new ArrayList<>();

}
