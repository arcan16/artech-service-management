package com.Ar_Tech.dto.serviceOrder;

import com.Ar_Tech.dto.serviceOrderImage.ImageByteWithMetadataDTO;
import com.Ar_Tech.dto.serviceOrderImage.ImageWithMetadataDTO;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.ServiceOrderImageEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public record FullServiceOrderDTO(Long id,
                                  String folio,
                                  Long customerDeviceId,
                                  String problemDescription,
                                  String diagnosis,
                                  EServiceOrderStatus status,
                                  Long assignedTo,
                                  LocalDateTime receivedAt,
                                  LocalDateTime deliveredAt,
                                  Long createdBy,
                                  String createdBySnapshot,
                                  BigDecimal estimatedCost,
                                  List<ImageByteWithMetadataDTO> imageWithMetadata) {

    public FullServiceOrderDTO(ServiceOrderEntity so) {
        this(so.getId(), so.getFolio(), so.getCustomerDevice().getId(), so.getProblemDescription(), so.getDiagnosis(),
                so.getStatus(),
                so.getAssignedTo() != null ? so.getAssignedTo().getId(): null,
                so.getReceivedAt(), so.getDeliveredAt(), so.getCreatedBy().getId(),
                so.getCreatedBySnapshot(), so.getEstimatedCost(),
                so.getImages().stream().map(ImageByteWithMetadataDTO::new).toList());
    }

    private static byte[] getImageBytes(String imgPath){
        try {
            return Files.readAllBytes(Paths.get(imgPath));
        }catch (Exception e){
            return null;
        }
    }
}
