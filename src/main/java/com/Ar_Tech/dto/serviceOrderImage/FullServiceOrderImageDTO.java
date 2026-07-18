package com.Ar_Tech.dto.serviceOrderImage;

import com.Ar_Tech.models.ServiceOrderImageEntity;
import com.Ar_Tech.models.enums.EImageType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public record FullServiceOrderImageDTO(Long id,
                                       Long serviceOrderId,
                                       EImageType imageType,
                                       String description,
                                       Long takenById,
                                       String takenBySnapshot,
                                       LocalDateTime createdAt,
                                       byte[] image
                                       ) {

    public FullServiceOrderImageDTO (ServiceOrderImageEntity soImage){
        this(soImage.getId(), soImage.getServiceOrder().getId(), soImage.getImageType(),
                soImage.getDescription(),  soImage.getTakenBy().getId(),soImage.getTakenBySnapshot(), soImage.getCreatedAt(),
                getImageBytes(soImage.getImagePath()));
    }

    private static byte[] getImageBytes(String imgPath){
        try {
            return Files.readAllBytes(Path.of(imgPath));
        } catch (IOException e) {
            return null;
        }
    }
}
