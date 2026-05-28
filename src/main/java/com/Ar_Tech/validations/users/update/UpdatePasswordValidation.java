package com.Ar_Tech.validations.users.update;

import com.Ar_Tech.dto.users.UserFullDTO;
import com.Ar_Tech.validations.users.PasswordValidationRules;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdatePasswordValidation implements IUpdateUserValidation{

    @Override
    public void validate(UserFullDTO userFullDTO, HttpServletRequest request) {
        if(userFullDTO.password() == null){
            return;
        }
        PasswordValidationRules passwordRules = new PasswordValidationRules(userFullDTO);
        passwordRules.validation();
    }
}
