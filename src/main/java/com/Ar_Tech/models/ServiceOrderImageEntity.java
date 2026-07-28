package com.Ar_Tech.models;

import com.Ar_Tech.dto.serviceOrderImage.CreationServiceOrderImageDTO;
import com.Ar_Tech.dto.serviceOrderImage.ImageWithMetadataDTO;
import com.Ar_Tech.models.enums.EImageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_order_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"serviceOrder","takenBy",})
public class ServiceOrderImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrderEntity serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_history_id")
    private ServiceOrderHistoryEntity serviceOrderHistory;

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
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    public ServiceOrderImageEntity(CreationServiceOrderImageDTO imageDTO) {
        this.serviceOrder = imageDTO.serviceOrder();
        this.imagePath = imageDTO.imagePath();
        this.imageType = imageDTO.imageType();
        this.description = imageDTO.description();
        this.takenBy = imageDTO.takenBy();
        this.takenBySnapshot = imageDTO.takenBySnapshot();
    }

    public ServiceOrderImageEntity(ServiceOrderEntity serviceOrder, ImageWithMetadataDTO imageWithMetadata,
                                   UserEntity author, Path imagePath) {
        this.serviceOrder = serviceOrder;
        this.imagePath = imagePath.toString();
        this.imageType = imageWithMetadata.imageType();
        this.description = imageWithMetadata.description();
        this.takenBy = author;
        this.takenBySnapshot = author.getPerson().getFirstName() + " " + author.getPerson().getLastName();
    }

    public ServiceOrderImageEntity(ServiceOrderEntity serviceOrder, ImageWithMetadataDTO imageWithMetadata,
                                   UserEntity author, Path imagePath, ServiceOrderHistoryEntity serviceOrderHistory) {
        this.serviceOrder = serviceOrder;
        this.serviceOrderHistory = serviceOrderHistory;
        this.imagePath = imagePath.toString();
        this.imageType = imageWithMetadata.imageType();
        this.description = imageWithMetadata.description();
        this.takenBy = author;
        this.takenBySnapshot = author.getPerson().getFirstName() + " " + author.getPerson().getLastName();
    }
}
