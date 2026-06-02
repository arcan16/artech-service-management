package com.Ar_Tech.validations.devices.create;

import com.Ar_Tech.dto.device.CreateDeviceDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface ICreateDeviceValidation {

    void validate(CreateDeviceDTO createDeviceDTO);
}
