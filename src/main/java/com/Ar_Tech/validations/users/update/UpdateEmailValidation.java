package com.Ar_Tech.validations.users.update;

import com.Ar_Tech.dto.users.UserFullDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.PersonRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateEmailValidation implements IUpdateUserValidation{

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void validate(UserFullDTO userFullDTO, HttpServletRequest request) {
        if(personRepository.existsByEmailAndIdNot(userFullDTO.email(), userFullDTO.id())){
            throw new MyIntegrityValidation("Error en correo electrónico, favor de verificar",400);
        }
    }
}
