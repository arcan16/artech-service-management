package com.Ar_Tech.dto.serviceOrderHistory;

import com.Ar_Tech.dto.serviceOrderImage.ImageByteWithMetadataDTO;
import com.Ar_Tech.models.ServiceOrderHistoryEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateServiceOrderHistoryDTO(@NotNull Long id,
                                           EServiceOrderStatus status,
                                           String notes,
                                           List<MultipartFile> images) {
}
