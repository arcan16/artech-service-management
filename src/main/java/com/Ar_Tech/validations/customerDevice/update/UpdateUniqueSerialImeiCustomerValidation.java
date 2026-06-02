package com.Ar_Tech.validations.customerDevice.update;

import com.Ar_Tech.dto.customerDevice.UpdateCustomerDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.CustomerDeviceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateUniqueSerialImeiCustomerValidation implements IUpdateCustomerDevieValidation {

    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;

    @Override
    public void validate(UpdateCustomerDeviceDTO customerDeviceDTO, HttpServletRequest request) {
        if(customerDeviceDTO.imei() == null && customerDeviceDTO.serialNumber() == null)
            throw new MyIntegrityValidation("Es obligatorio colocar el numero de serie o el IMEI", 400);

        if(customerDeviceDTO.serialNumber() != null && customerDeviceDTO.imei() != null){
            if(customerDeviceRepository.existsBySerialNumberAndImeiAndIdNot(customerDeviceDTO.serialNumber(), customerDeviceDTO.imei(),
            customerDeviceDTO.id())){
                throw new MyIntegrityValidation("El dispositivo ya existe", 400);
            }
        }else if(customerDeviceDTO.imei() != null){
            if(customerDeviceRepository.existsByImeiAndIdNot(customerDeviceDTO.imei(), customerDeviceDTO.id())){
                throw new MyIntegrityValidation("El IMEI ya se encuentra registrado",400);
            }
        }else if(customerDeviceRepository.existsBySerialNumberAndIdNot(customerDeviceDTO.serialNumber(), customerDeviceDTO.id())){
            throw new MyIntegrityValidation("El numero de serie ya se encuentra registrado",400);
        }
    }
}
