package com.Ar_Tech.validations.customerDevice.create;

import com.Ar_Tech.dto.customerDevice.CreateCustomerDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.CustomerDeviceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueSerialImeiCustomerDeviceValidation implements ICreateCustomerDeviceValidation{

    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;

    @Override
    public void validate(CreateCustomerDeviceDTO createCustomerDeviceDTO, HttpServletRequest request) {
        if(createCustomerDeviceDTO.imei() == null && createCustomerDeviceDTO.serialNumber() == null)
            throw new MyIntegrityValidation("Es obligatorio colocar el numero de serie o el IMEI", 400);

        if(createCustomerDeviceDTO.serialNumber() != null && createCustomerDeviceDTO.imei() != null){
            if(customerDeviceRepository.existsBySerialNumberAndImei(createCustomerDeviceDTO.serialNumber(), createCustomerDeviceDTO.imei())){
                throw new MyIntegrityValidation("El dispositivo ya existe", 400);
            }
        }else if(createCustomerDeviceDTO.imei() != null){
            if(customerDeviceRepository.existsByImei(createCustomerDeviceDTO.imei())){
                throw new MyIntegrityValidation("El IMEI ya se encuentra registrado",400);
            }
        }else if(customerDeviceRepository.existsBySerialNumber(createCustomerDeviceDTO.serialNumber())){
            throw new MyIntegrityValidation("El numero de serie ya se encuentra registrado",400);
        }
    }
}
