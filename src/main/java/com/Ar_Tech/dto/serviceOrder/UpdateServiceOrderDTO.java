package com.Ar_Tech.dto.serviceOrder;

import com.Ar_Tech.models.enums.EServiceOrderStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateServiceOrderDTO(@NotNull Long id,
                                    Long customerDeviceId,
                                    String problemDescription,
                                    String diagnosis,
                                    EServiceOrderStatus status,
                                    Long assignedTo,
                                    LocalDate estimatedDelivery,
                                    LocalDate deliveredAt,
                                    BigDecimal estimatedCost,
                                    List<MultipartFile> images
                                    ) {
}