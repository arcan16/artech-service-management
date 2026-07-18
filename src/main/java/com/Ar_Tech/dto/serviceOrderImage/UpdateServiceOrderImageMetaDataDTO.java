package com.Ar_Tech.dto.serviceOrderImage;

import com.Ar_Tech.models.enums.EAlterImage;
import com.Ar_Tech.models.enums.EImageType;

public record UpdateServiceOrderImageMetaDataDTO(Long id,
                                                 String name,
                                                 EImageType imageType,
                                                 String description,
                                                 EAlterImage alter) implements IImageMetadata{
}