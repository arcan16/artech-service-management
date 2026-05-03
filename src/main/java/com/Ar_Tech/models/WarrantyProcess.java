package com.Ar_Tech.models;
import com.Ar_Tech.models.enums.EWarrantyResolution;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_process")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private Returns returns;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = true)
    private ServiceOrder serviceOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution")
    private EWarrantyResolution resolution;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}