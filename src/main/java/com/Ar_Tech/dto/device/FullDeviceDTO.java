package com.Ar_Tech.dto.device;

import com.Ar_Tech.models.DeviceEntity;

import java.time.LocalDateTime;

public record FullDeviceDTO(Long id,
                            String brand,
                            String model,
                            LocalDateTime createdAt) {
    public FullDeviceDTO(DeviceEntity newDevice) {
        this(newDevice.getId(), newDevice.getBrand(), newDevice.getModel(), newDevice.getCreatedAt());
    }
}
