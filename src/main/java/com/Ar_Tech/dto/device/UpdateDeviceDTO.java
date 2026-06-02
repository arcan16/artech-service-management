package com.Ar_Tech.dto.device;

import com.Ar_Tech.models.DeviceEntity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateDeviceDTO(@NotNull Long id,
                              String brand,
                              String model
                              ) {
}
