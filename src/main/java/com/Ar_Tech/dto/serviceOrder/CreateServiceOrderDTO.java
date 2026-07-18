package com.Ar_Tech.dto.serviceOrder;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateServiceOrderDTO(Long customerDevice,
                                    String problemDescription,
                                    LocalDate estimatedDelivery,
                                    String diagnosis,
                                    BigDecimal estimatedCost,
                                    List<MultipartFile> images
                                    ) {
}