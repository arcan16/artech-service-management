package com.Ar_Tech.models;
import com.Ar_Tech.models.Client;
import com.Ar_Tech.models.Sales;
import com.Ar_Tech.models.enums.CreditStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCredits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    @NotNull
    private Sales sale;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.0")
    private BigDecimal totalAmount;

    @Column(name = "balance", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.0")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CreditStatus status;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Helpers para lógica de negocio
    @Transient
    public boolean isOverdue() {
        return CreditStatus.OVERDUE.equals(status) ||
                (dueDate != null && dueDate.isBefore(LocalDate.now()) && balance.compareTo(BigDecimal.ZERO) > 0);
    }

    @Transient
    public boolean isPaid() {
        return balance.compareTo(BigDecimal.ZERO) == 0;
    }
}