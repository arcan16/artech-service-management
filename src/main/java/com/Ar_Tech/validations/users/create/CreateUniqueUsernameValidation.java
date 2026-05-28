package com.Ar_Tech.validations.users.create;

import com.Ar_Tech.dto.users.UserDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateUniqueUsernameValidation implements IUserValidation {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(UserDTO userDTO, HttpServletRequest request) {
        if(userRepository.existsByUsername(userDTO.username())){
            throw new MyIntegrityValidation("El username ya existe",400);
        }
    }
}
