package com.Ar_Tech.validations.serviceOrder.create;

import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.CustomerDeviceEntity;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.repositories.CustomerDeviceRepository;
import com.Ar_Tech.repositories.ServiceOrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotEnteredValidation implements IServiceOrderValidation{

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;

    @Override
    public void validate(CreateServiceOrderDTO serviceOrderDTO, HttpServletRequest request) {
        CustomerDeviceEntity customerDevice = customerDeviceRepository.findById(serviceOrderDTO.customerDevice())
                .orElseThrow(()-> new MyIntegrityValidation("Error: El dispositivo del cliente no existe", 400));

        Optional<ServiceOrderEntity> serviceOrderEntity = serviceOrderRepository.findByCustomerDeviceAndDeliveredAtIsNull(customerDevice);

        if(serviceOrderEntity.isPresent()){
            throw new MyIntegrityValidation("Error: El dispositivo ya se encuentra ingresado",400);
        }
    }
}
