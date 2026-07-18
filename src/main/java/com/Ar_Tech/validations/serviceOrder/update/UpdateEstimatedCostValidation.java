package com.Ar_Tech.validations.serviceOrder.update;

import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.validations.serviceOrder.create.IServiceOrderValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UpdateEstimatedCostValidation implements IUpdateServiceOrderValidation {

    @Override
    public void validation(UpdateServiceOrderDTO updateServiceOrderDTO, ServiceOrderEntity serviceOrder, HttpServletRequest request) {
        if(updateServiceOrderDTO.estimatedCost() == null)
            return;

        if(updateServiceOrderDTO.estimatedCost().compareTo(BigDecimal.ZERO) <= 0)
            throw new MyIntegrityValidation("Error: El costo estimado no puede ser inferior a $0.00, favor de verificar",400);
    }
}
