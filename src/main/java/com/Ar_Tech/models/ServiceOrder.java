package com.Ar_Tech.models;

import com.Ar_Tech.models.enums.ServiceOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folio", length = 50, unique = true)
    private String folio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_device_id", nullable = false)
    private CustomerDevice customerDevice;

    @Lob
    @Column(name = "problem_description", columnDefinition = "TEXT")
    private String problemDescription;

    @Lob
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ServiceOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_by_snapshot", length = 150)
    private String createdBySnapshot;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;
}