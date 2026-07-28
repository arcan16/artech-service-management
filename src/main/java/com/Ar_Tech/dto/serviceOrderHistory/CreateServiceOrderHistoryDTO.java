package com.Ar_Tech.dto.serviceOrderHistory;

import com.Ar_Tech.models.enums.EServiceOrderStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateServiceOrderHistoryDTO(@NotNull Long serviceOrderId,
                                           @NotNull EServiceOrderStatus status,
                                           @NotNull String notes,
                                           @NotNull List<MultipartFile> images) {
}
