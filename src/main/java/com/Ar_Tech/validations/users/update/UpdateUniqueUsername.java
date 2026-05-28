package com.Ar_Tech.validations.users.update;

import com.Ar_Tech.dto.users.UserFullDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateUniqueUsername implements IUpdateUserValidation{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(UserFullDTO userFullDTO, HttpServletRequest request) {
        if(userRepository.existsByUsernameAndIdNot(userFullDTO.username(), userFullDTO.id())){
            throw new MyIntegrityValidation("El nombre de usuario no esta disponible",400);
        }
    }
}
