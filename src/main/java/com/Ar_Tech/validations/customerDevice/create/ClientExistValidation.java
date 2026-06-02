package com.Ar_Tech.validations.customerDevice.create;

import com.Ar_Tech.dto.customerDevice.CreateCustomerDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.ClientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientExistValidation implements  ICreateCustomerDeviceValidation{

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void validate(CreateCustomerDeviceDTO createCustomerDeviceDTO, HttpServletRequest request) {
        if(!clientRepository.existsById(createCustomerDeviceDTO.client())){
            throw new MyIntegrityValidation("Error: El cliente indicado no existe",400);
        }
    }
}
