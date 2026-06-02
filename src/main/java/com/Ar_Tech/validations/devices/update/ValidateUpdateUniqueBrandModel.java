package com.Ar_Tech.validations.devices.update;

import com.Ar_Tech.dto.device.UpdateDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.DeviceEntity;
import com.Ar_Tech.repositories.DeviceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUpdateUniqueBrandModel implements IUpdateDeviceValidation{

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void validate(UpdateDeviceDTO deviceDTO, HttpServletRequest request, DeviceEntity device) {
        // Llego Brand y Model
        if(deviceDTO.brand() != null && deviceDTO.model() != null){
            if(deviceRepository.existsByBrandAndModelAndIdNot(deviceDTO.brand(), deviceDTO.model(), deviceDTO.id())){
                throw new MyIntegrityValidation("Error, el registro ya existe",400);
            }
        }
        // Solo llego Brand, completar model
        else if(deviceDTO.brand() != null){
            if(deviceRepository.existsByBrandAndModelAndIdNot(deviceDTO.brand(), device.getModel(), deviceDTO.id())){
                throw new MyIntegrityValidation("Error, el registro ya existe",400);
            }
        }
        // Solo llego Model, completar Brand
        else{
            if(deviceRepository.existsByBrandAndModelAndIdNot(device.getBrand(), deviceDTO.model(), deviceDTO.id())){
                throw new MyIntegrityValidation("Error, el registro ya existe",400);
            }
        }


    }
}
