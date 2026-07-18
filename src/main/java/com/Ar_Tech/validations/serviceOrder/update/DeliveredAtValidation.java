package com.Ar_Tech.validations.serviceOrder.update;

import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class DeliveredAtValidation implements  IUpdateServiceOrderValidation {

    @Override
    public void validation(UpdateServiceOrderDTO updateServiceOrderDTO, ServiceOrderEntity serviceOrder, HttpServletRequest request) {
        if(updateServiceOrderDTO.deliveredAt() == null){
            return;
        }
        if(updateServiceOrderDTO.deliveredAt().isBefore(serviceOrder.getReceivedAt().toLocalDate()))
            throw new MyIntegrityValidation("Error: La fecha de entrega es incorrecta, favor de verificar",400);
    }
}
