package com.Ar_Tech.dto.customerDevice;

import jakarta.validation.constraints.NotNull;

public record UpdateCustomerDeviceDTO(@NotNull Long id,
                                      Long client,
                                      Long device,
                                      String serialNumber,
                                      String imei) {
}