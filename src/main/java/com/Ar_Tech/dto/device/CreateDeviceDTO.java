package com.Ar_Tech.dto.device;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public record CreateDeviceDTO(String brand,
                              String model
) {
}
