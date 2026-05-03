package com.Ar_Tech.validations.persons.create;

import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.PersonRepository;
import com.Ar_Tech.validations.persons.create.IPersonValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("createPersonValidators")
public class CreateUniqueEmailValidation implements IPersonValidation {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void validate(PersonDTO personDTO) {
        if(personRepository.existsByEmail(personDTO.email()))
            throw new MyIntegrityValidation("El email ya se encuentra registrado en con otra cuenta, favor de revisar!", 400);
    }
}
