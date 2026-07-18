package com.Ar_Tech.validations.serviceOrder.create;

import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EstimatedDeliveryValidation implements IServiceOrderValidation{
    @Override
    public void validate(CreateServiceOrderDTO serviceOrderDTO, HttpServletRequest request) {
        if(serviceOrderDTO.estimatedDelivery() == null)
            return;
        if(serviceOrderDTO.estimatedDelivery().isBefore(LocalDate.now()) ||
                serviceOrderDTO.estimatedDelivery().isEqual(LocalDate.now()))
            throw new MyIntegrityValidation("Fecha incorrecta, favor de verificar",400);
    }
}
