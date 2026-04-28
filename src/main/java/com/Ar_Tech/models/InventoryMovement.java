package com.Ar_Tech.models;

import com.Ar_Tech.models.Warehouse;
import com.Ar_Tech.models.PartsProducts;
import com.Ar_Tech.models.User;
import com.Ar_Tech.models.enums.MovementReferenceType;
import com.Ar_Tech.models.enums.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private MovementReferenceType referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PartsProducts product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;

    @Column(name = "created_by_snapshot", length = 150)
    private String createdBySnapshot;

    @Column(name = "reference", length = 255)
    private String reference;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
