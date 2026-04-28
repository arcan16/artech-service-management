package com.Ar_Tech.models;

import com.Ar_Tech.models.ServiceOrder;
import com.Ar_Tech.models.User;
import com.Ar_Tech.models.enums.AuthorizationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_order_authorization")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrder serviceOrder;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    @Lob
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "authorization_status", nullable = false)
    private AuthorizationStatus authorizationStatus;

    @Column(name = "authorized_at")
    private LocalDateTime authorizedAt;

    @Lob
    @Column(name = "client_response", columnDefinition = "TEXT")
    private String clientResponse;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;

    @Column(name = "created_by_snapshot", length = 150)
    private String createdBySnapshot;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
