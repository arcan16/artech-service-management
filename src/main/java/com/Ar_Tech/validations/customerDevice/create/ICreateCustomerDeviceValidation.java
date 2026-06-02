package com.Ar_Tech.validations.customerDevice.create;

import com.Ar_Tech.dto.customerDevice.CreateCustomerDeviceDTO;
import com.Ar_Tech.dto.customerDevice.FullCustomerDeviceDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface ICreateCustomerDeviceValidation {

    void validate(CreateCustomerDeviceDTO createCustomerDeviceDTO, HttpServletRequest request);
}
