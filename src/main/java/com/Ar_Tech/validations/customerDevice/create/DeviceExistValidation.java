package com.Ar_Tech.validations.customerDevice.create;

import com.Ar_Tech.dto.customerDevice.CreateCustomerDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.DeviceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceExistValidation implements ICreateCustomerDeviceValidation{

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void validate(CreateCustomerDeviceDTO createCustomerDeviceDTO, HttpServletRequest request) {
        if(!deviceRepository.existsById(createCustomerDeviceDTO.device())){
            throw new MyIntegrityValidation("Error: El dispositivo indicado no existe",400);
        }
    }
}
