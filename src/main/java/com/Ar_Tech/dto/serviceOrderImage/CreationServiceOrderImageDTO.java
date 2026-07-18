package com.Ar_Tech.dto.serviceOrderImage;

import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EImageType;

public record CreationServiceOrderImageDTO(ServiceOrderEntity serviceOrder,
                                           String imagePath,
                                           EImageType imageType,
                                           String description,
                                           UserEntity takenBy,
                                           String takenBySnapshot) {
    public CreationServiceOrderImageDTO(ServiceOrderEntity serviceOrder, String imagePath, EImageType imageType,
                                        CreateServiceOrderImageMetaDataDTO soi, UserEntity author) {
        this(serviceOrder, imagePath, imageType, soi.description(), author,
                author.getPerson().getFirstName() + " " + author.getPerson().getLastName());

    }
}
