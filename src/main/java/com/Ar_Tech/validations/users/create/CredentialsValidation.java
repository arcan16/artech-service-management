package com.Ar_Tech.validations.users.create;

import com.Ar_Tech.dto.users.UserDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CredentialsValidation implements IUserValidation{

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void validate(UserDTO userDTO, HttpServletRequest request) {
        UserEntity author = jwtUtils.getUserFromRequest(request);
        if(author.getRole().ordinal()> userDTO.role().ordinal()){
            throw new MyIntegrityValidation("No tienes privilegios para crear un usuario con este rol",400);
        }
    }
}
