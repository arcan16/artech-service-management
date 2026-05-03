package com.Ar_Tech.models;

import com.Ar_Tech.models.enums.EImageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_order_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrder serviceOrder;

    @Column(name = "image_path", nullable = false, length = 500)
    private String imagePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private EImageType imageType;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taken_by", nullable = true)
    private UserEntity takenBy;

    @Column(name = "taken_by_snapshot", length = 150)
    private String takenBySnapshot;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
