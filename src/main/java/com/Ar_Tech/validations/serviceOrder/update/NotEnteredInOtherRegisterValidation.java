package com.Ar_Tech.validations.serviceOrder.update;

import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import com.Ar_Tech.repositories.ServiceOrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotEnteredInOtherRegisterValidation implements IUpdateServiceOrderValidation{

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Override
    public void validation(UpdateServiceOrderDTO updateServiceOrderDTO, ServiceOrderEntity serviceOrder, HttpServletRequest request) {
        if(updateServiceOrderDTO.customerDeviceId() == null) // Si el valor no es recibido no tiene sentido validar
            return;

        if(serviceOrderRepository.existsByCustomerDeviceIdAndIdNotAndStatusNot(updateServiceOrderDTO.customerDeviceId(),
                updateServiceOrderDTO.id(), EServiceOrderStatus.DELIVERED)){
            throw new MyIntegrityValidation("Error: El dispositivo se encuentra registrado en otra orden de servicio",400);
        }
    }
}
