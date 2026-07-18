package com.Ar_Tech.validations.serviceOrder.update;

import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IsUserToAssignValidation implements IUpdateServiceOrderValidation{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validation(UpdateServiceOrderDTO updateServiceOrderDTO, ServiceOrderEntity serviceOrder, HttpServletRequest request) {
        if(updateServiceOrderDTO.assignedTo() == null)
            return;

        UserEntity userAssignation = userRepository.findById(updateServiceOrderDTO.assignedTo())
                .orElseThrow(()-> new MyIntegrityValidation("Error: El tecnico al que se pretende asignar no es valido", 400));
    }
}
