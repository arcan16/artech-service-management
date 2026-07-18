package com.Ar_Tech.dto.serviceOrderImage;

import com.Ar_Tech.models.ServiceOrderImageEntity;
import com.Ar_Tech.models.enums.EAlterImage;
import com.Ar_Tech.models.enums.EImageType;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public record ImageWithMetadataDTO(Long id,
                                   String name,
                                   EImageType imageType,
                                   String description,
                                   EAlterImage alter,
                                   MultipartFile image) {
    /*public ImageWithMetadataDTO(MultipartFile image, UpdateServiceOrderImageMetaDataDTO imageMetaDataDTO) {
        this(imageMetaDataDTO.id() != null ? imageMetaDataDTO.id() : null, imageMetaDataDTO.name(), imageMetaDataDTO.imageType(), imageMetaDataDTO.description(),
                imageMetaDataDTO.alter(), image);

    }*/
    public ImageWithMetadataDTO(
            MultipartFile image,
            IImageMetadata metadata) {

        this(
                metadata.id(),
                metadata.name(),
                metadata.imageType(),
                metadata.description(),
                metadata.alter(),
                image
        );
    }
}
