package com.Ar_Tech.dto.serviceOrderImage;

import com.Ar_Tech.models.enums.EAlterImage;
import com.Ar_Tech.models.enums.EImageType;

public interface IImageMetadata {

    String name();

    EImageType imageType();

    String description();

    default Long id() {
        return null;
    }

    default EAlterImage alter() {
        return null;
    }
}