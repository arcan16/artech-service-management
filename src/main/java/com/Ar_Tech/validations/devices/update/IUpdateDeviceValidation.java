package com.Ar_Tech.validations.devices.update;

import com.Ar_Tech.dto.device.UpdateDeviceDTO;
import com.Ar_Tech.models.DeviceEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface IUpdateDeviceValidation {

    void validate(UpdateDeviceDTO deviceDTO, HttpServletRequest request, DeviceEntity device);
}
