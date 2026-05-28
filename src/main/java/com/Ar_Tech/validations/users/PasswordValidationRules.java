package com.Ar_Tech.validations.users;

import com.Ar_Tech.dto.users.UserDTO;
import com.Ar_Tech.dto.users.UserFullDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidationRules {

    private final String password;

    public PasswordValidationRules(UserFullDTO userData) {
        this.password = userData.password();
    }

    public PasswordValidationRules(UserDTO userDTO){
        this.password = userDTO.password();
    }

    public void validation(){
        ArrayList<String> errorList = new ArrayList<>();

        Pattern numberPattern = Pattern.compile("\\d{1,}");
        Matcher numberMatcher = numberPattern.matcher(password);

        if(!numberMatcher.find()){
            errorList.add("El password debe contener al menos un numero\n");
        }

        Pattern specialCharacterPattern = Pattern.compile("[!@#$%^&*()_+{}:;<>,.?~\\-]+");
        Matcher specialCharacterMatcher = specialCharacterPattern.matcher(password);

        if(!specialCharacterMatcher.find()){
            errorList.add("El password debe contener al menos un caracter especial: '!@#$%^&*()_+{}:;<>,.?~\\-' \n");
        }

        Pattern upperCasePattern = Pattern.compile("[A-Z]+");
        Matcher upperCaseMatcher = upperCasePattern.matcher(password);

        if(!upperCaseMatcher.find()){
            errorList.add("Debe contener al menos una letra mayuscula");
        }

        if(!errorList.isEmpty()){
            throw new MyIntegrityValidation(errorList.toString(), 400);
        }
    }
}
