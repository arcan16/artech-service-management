package com.Ar_Tech.validations.persons.create;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.validations.persons.create.IPersonValidation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Qualifier("createPersonValidators")
public class CreatePhoneValidation implements IPersonValidation {
    @Override
    public void validate(PersonDTO personDTO) {
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(personDTO.phone());

        if(!matcher.matches()){
            throw new MyIntegrityValidation("El numero de telefono debe contener 10 digitos",400);
        }
    }
}
