package com.Ar_Tech.models;
import com.Ar_Tech.models.enums.EReturnStatus;
import com.Ar_Tech.models.enums.EReturnType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Returns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    @NotNull
    private Sales sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_type", nullable = false)
    @NotNull
    private EReturnType returnType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EReturnStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Helper para flujo de negocio
    @Transient
    public boolean isPendingReview() {
        return EReturnStatus.UNDER_REVIEW.equals(status) ||
                (status == null && EReturnType.WARRANTY.equals(returnType));
    }
}