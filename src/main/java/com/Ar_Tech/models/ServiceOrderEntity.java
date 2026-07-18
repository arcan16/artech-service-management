package com.Ar_Tech.models;

import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folio", length = 50, unique = true)
    private String folio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_device_id", nullable = false)
    private CustomerDeviceEntity customerDevice;

    @Lob
    @Column(name = "problem_description", columnDefinition = "TEXT")
    private String problemDescription;

    @Lob
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EServiceOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private UserEntity assignedTo;

    @Column(name = "received_at")
    @CreatedDate
    private LocalDateTime receivedAt = LocalDateTime.now();

    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @Column(name = "created_by_snapshot", length = 150)
    private String createdBySnapshot;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    @OneToMany(mappedBy = "serviceOrder",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ServiceOrderImageEntity> images = new ArrayList<>();

    public ServiceOrderEntity(String folio, CreateServiceOrderDTO serviceOrderDTO, CustomerDeviceEntity customerDevice,
                              UserEntity author) {
        this.folio = folio;
        this.customerDevice = customerDevice;
        this.problemDescription = serviceOrderDTO.problemDescription();
        this.diagnosis = serviceOrderDTO.diagnosis();
        this.estimatedCost = serviceOrderDTO.estimatedCost();
        this.estimatedDelivery = serviceOrderDTO.estimatedDelivery().atStartOfDay();
        this.status = EServiceOrderStatus.RECEIVED;
        this.createdBy = author;
        this.createdBySnapshot = author.getPerson().getFirstName() + " " +author.getPerson().getLastName();
    }

    public void update(@Valid UpdateServiceOrderDTO serviceOrder) {

        if(serviceOrder.problemDescription() != null)
            this.problemDescription = serviceOrder.problemDescription();

        if(serviceOrder.diagnosis() != null)
            this.diagnosis = serviceOrder.diagnosis();

        if(serviceOrder.estimatedCost() != null)
            this.estimatedCost = serviceOrder.estimatedCost();

        if(serviceOrder.estimatedDelivery() != null)
            this.estimatedDelivery = serviceOrder.estimatedDelivery().atStartOfDay();

        if(serviceOrder.status() != null)
            this.status = serviceOrder.status();
    }
}