package com.Ar_Tech.models;

import com.Ar_Tech.models.enums.EMovementReferenceType;
import com.Ar_Tech.models.enums.EMovementType;
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
    private EMovementReferenceType referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PartsProducts product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private EMovementType movementType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = true)
    private UserEntity createdBy;

    @Column(name = "created_by_snapshot", length = 150)
    private String createdBySnapshot;

    @Column(name = "reference", length = 255)
    private String reference;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
