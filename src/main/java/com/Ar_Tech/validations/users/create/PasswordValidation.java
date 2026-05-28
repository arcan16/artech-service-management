package com.Ar_Tech.validations.users.create;

import com.Ar_Tech.dto.users.UserDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.validations.users.PasswordValidationRules;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidation implements IUserValidation{
    @Override
    public void validate(UserDTO userDTO, HttpServletRequest request) {
        PasswordValidationRules passwordValidationRules = new PasswordValidationRules(userDTO);

        passwordValidationRules.validation();

    }
}
