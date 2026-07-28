package com.Ar_Tech.validations.serviceOrderHistory;

import com.Ar_Tech.dto.serviceOrderHistory.CreateServiceOrderHistoryDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import com.Ar_Tech.models.enums.EUserRole;
import org.springframework.stereotype.Component;

@Component
public class UserAuthorityValidation implements IServiceOrderHistoryValidations{
    /// Solo el técnico al que le fue asignado el servicio o el admin podrá crear ServiceOrderHistory
    @Override
    public void validate(EServiceOrderStatus status, UserEntity author, ServiceOrderEntity serviceOrder) {
        /// Se permite la acción si el author es un ADMIN
        if(author.getRole().equals(EUserRole.ADMIN))
            return;
        /// Solo el usuario al que se le asigno el servicio puede generarle un historial
        if(!serviceOrder.getAssignedTo().equals(author)){
            throw new MyIntegrityValidation("Error: No tienes las credenciales para realizar esta acción", 400);
        }
    }
}
