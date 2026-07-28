package com.Ar_Tech.validations.serviceOrderHistory;

import com.Ar_Tech.dto.serviceOrderHistory.CreateServiceOrderHistoryDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import org.springframework.stereotype.Component;

@Component
public class StatusValidation implements IServiceOrderHistoryValidations{
    /// Solo se permite generar un registro a los serviceOrders que no se hayan entregado
    @Override
    public void validate(EServiceOrderStatus status, UserEntity author, ServiceOrderEntity serviceOrder) {
        if(serviceOrder.getStatus().equals(EServiceOrderStatus.DELIVERED))
            throw new MyIntegrityValidation("Error: La orden de servicio ya fue entregada",400);

        if(status.equals(EServiceOrderStatus.RECEIVED))
            throw new MyIntegrityValidation("Error: No es posible asignar el status 'RECEIVED' al crear un historial",400);
    }
}
