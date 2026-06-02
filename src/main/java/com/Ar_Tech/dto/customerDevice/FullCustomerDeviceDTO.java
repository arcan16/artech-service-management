package com.Ar_Tech.dto.customerDevice;

import com.Ar_Tech.models.CustomerDeviceEntity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FullCustomerDeviceDTO(Long id,
                                    Long client,
                                    Long device,
                                    String serialNumber,
                                    String imei,
                                    LocalDateTime created) {
    public FullCustomerDeviceDTO(CustomerDeviceEntity customerDevice) {
        this(customerDevice.getId(), customerDevice.getClient().getId(), customerDevice.getDevice().getId(),
                customerDevice.getSerialNumber() != null? customerDevice.getSerialNumber(): null,
                customerDevice.getImei() != null ? customerDevice.getImei(): null, customerDevice.getCreatedAt());
    }
}