package com.Ar_Tech.models;

import com.Ar_Tech.models.ServiceOrder;
import com.Ar_Tech.models.PartsProducts;
import com.Ar_Tech.models.enums.UsageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "service_order_products_parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderProductsParts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrder serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_product_id", nullable = false)
    private PartsProducts partProduct;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_type")
    private UsageType usageType;
}
