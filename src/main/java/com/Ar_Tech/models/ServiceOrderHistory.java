package com.Ar_Tech.models;


import com.Ar_Tech.models.enums.ServiceOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_order_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ServiceOrderStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(name = "changed_by_snapshot", length = 150)
    private String changedBySnapshot;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}