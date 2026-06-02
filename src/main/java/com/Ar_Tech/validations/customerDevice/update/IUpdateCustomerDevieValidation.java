package com.Ar_Tech.validations.customerDevice.update;

import com.Ar_Tech.dto.customerDevice.UpdateCustomerDeviceDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IUpdateCustomerDevieValidation {

    void validate(UpdateCustomerDeviceDTO customerDeviceDTO, HttpServletRequest request);
}
