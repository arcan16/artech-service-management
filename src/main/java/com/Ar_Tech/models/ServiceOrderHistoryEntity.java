package com.Ar_Tech.models;


import com.Ar_Tech.dto.serviceOrderHistory.CreateServiceOrderHistoryDTO;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_order_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id")
    private ServiceOrderEntity serviceOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EServiceOrderStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private UserEntity changedBy;

    @Column(name = "changed_by_snapshot", length = 150)
    private String changedBySnapshot;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "serviceOrderHistory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ServiceOrderImageEntity> images = new ArrayList<>();

    public ServiceOrderHistoryEntity(ServiceOrderEntity serviceOrder, @Valid CreateServiceOrderHistoryDTO serviceOrderHistoryData,
                                     UserEntity author) {
        this.serviceOrder = serviceOrder;
        this.serviceOrder.setStatus(serviceOrderHistoryData.status());
        this.status = serviceOrderHistoryData.status();
        this.notes = serviceOrderHistoryData.notes();
        this.changedBy = author;
        this.changedBySnapshot = author.getPerson().getFirstName() + " " + author.getPerson().getLastName() + " "  + author.getPerson().getEmail();
    }
}