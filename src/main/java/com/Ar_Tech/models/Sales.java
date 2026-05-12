package com.Ar_Tech.models;


import com.Ar_Tech.models.enums.ESaleStatus;
import com.Ar_Tech.models.enums.ESaleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_folio", length = 50, unique = true, nullable = false)
    private String saleFolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserEntity user;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.0")
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ESaleStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type", nullable = false)
    @NotNull
    private ESaleType saleType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}