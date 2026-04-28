package com.Ar_Tech.models;

import com.Ar_Tech.models.PartsProducts;
import com.Ar_Tech.models.Sales;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_parts_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalePartsProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    @NotNull
    private Sales sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @NotNull
    private PartsProducts item;

    @Column(name = "quantity", nullable = false)
    @Min(value = 1)
    private Integer quantity;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.0")
    private BigDecimal subtotal;

    // Método utilitario para calcular subtotal (opcional)
    @Transient
    public BigDecimal getCalculatedSubtotal() {
        return this.price != null && this.quantity != null
                ? this.price.multiply(BigDecimal.valueOf(this.quantity))
                : BigDecimal.ZERO;
    }
}
