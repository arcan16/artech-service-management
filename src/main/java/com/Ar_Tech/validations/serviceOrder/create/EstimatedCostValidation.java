package com.Ar_Tech.validations.serviceOrder.create;

import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EstimatedCostValidation implements IServiceOrderValidation{
    @Override
    public void validate(CreateServiceOrderDTO serviceOrderDTO, HttpServletRequest request) {
        if(serviceOrderDTO.estimatedCost() == null)
            return;

        if(serviceOrderDTO.estimatedCost().compareTo(BigDecimal.ZERO) <= 0)
            throw new MyIntegrityValidation("Error: El costo estimado es incorrecto, favor de verificar",400);
    }
}
