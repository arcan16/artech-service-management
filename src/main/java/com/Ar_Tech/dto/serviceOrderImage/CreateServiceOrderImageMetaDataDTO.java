package com.Ar_Tech.dto.serviceOrderImage;

import com.Ar_Tech.models.enums.EImageType;

public record CreateServiceOrderImageMetaDataDTO(String name,
                                                 EImageType imageType,
                                                 String description) implements IImageMetadata {
}