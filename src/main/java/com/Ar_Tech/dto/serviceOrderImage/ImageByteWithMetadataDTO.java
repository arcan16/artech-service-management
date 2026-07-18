package com.Ar_Tech.dto.serviceOrderImage;

import com.Ar_Tech.models.ServiceOrderImageEntity;
import com.Ar_Tech.models.enums.EAlterImage;
import com.Ar_Tech.models.enums.EImageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public record ImageByteWithMetadataDTO(Long id,
                                       String name,
                                       EImageType imageType,
                                       String description,
                                       byte[] image) {


    public ImageByteWithMetadataDTO(ServiceOrderImageEntity img) {
        this(img.getId(), new File(img.getImagePath()).getName(), img.getImageType(), img.getDescription(), getImageBytes(img.getImagePath()));
    }

    private static byte[] getImageBytes(String imgPath){
        try {
            return Files.readAllBytes(Paths.get(imgPath));
        }catch (Exception e){
            return null;
        }
    }
}
