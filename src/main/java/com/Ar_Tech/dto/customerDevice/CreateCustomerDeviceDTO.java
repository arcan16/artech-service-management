package com.Ar_Tech.dto.customerDevice;

import com.Ar_Tech.models.ClientEntity;
import com.Ar_Tech.models.DeviceEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerDeviceDTO(@NotNull Long client,
                                      @NotNull Long device,
                                      String serialNumber,
                                      String imei) {
}