package com.Ar_Tech.validations.devices.create;

import com.Ar_Tech.dto.device.CreateDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUniqueBrandModel implements ICreateDeviceValidation{

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void validate(CreateDeviceDTO createDeviceDTO) {
        if(deviceRepository.existsByBrandAndModel(createDeviceDTO.brand(), createDeviceDTO.model())){
            throw new MyIntegrityValidation("Error, el registro ya existe",400);
        }
    }
}
