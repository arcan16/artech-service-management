package com.Ar_Tech.dto.serviceOrderHistory;

import com.Ar_Tech.dto.serviceOrderImage.ImageByteWithMetadataDTO;
import com.Ar_Tech.models.ServiceOrderHistoryEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record FullServiceOrderHistoryDTO(Long id,
                                         Long serviceOrderId,
                                         EServiceOrderStatus status,
                                         String notes,
                                         Long changedBy,
                                         String changedBySnapshot,
                                         List<ImageByteWithMetadataDTO> imagesWithMetadata) {

    public FullServiceOrderHistoryDTO(ServiceOrderHistoryEntity sohData, UserEntity author) {
        this(sohData.getId(), sohData.getServiceOrder().getId(), sohData.getStatus(),
                sohData.getNotes(), author.getId(), author.getPerson().getFirstName() + " " + author.getPerson().getLastName(),
                sohData.getImages().stream().map(ImageByteWithMetadataDTO::new).toList());
    }

    public FullServiceOrderHistoryDTO(ServiceOrderHistoryEntity sohData) {
        this(sohData.getId(), sohData.getServiceOrder().getId(), sohData.getStatus(),
                sohData.getNotes(), sohData.getChangedBy().getId(), sohData.getChangedBy().getPerson().getFirstName() + " " + sohData.getChangedBy().getPerson().getLastName(),
                sohData.getImages().stream().map(ImageByteWithMetadataDTO::new).toList());
    }
}
