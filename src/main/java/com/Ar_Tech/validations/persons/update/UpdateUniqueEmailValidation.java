package com.Ar_Tech.validations.persons.update;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("createPersonValidators")
public class UpdateUniqueEmailValidation implements IUpdatePersonValidation {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void validate(FullPersonDTO personDTO) {
        if(personDTO.email() == null)
            return;

        if(personRepository.existsByEmailAndIdNot(personDTO.email(), personDTO.id())){
            throw new MyIntegrityValidation("El email ya se encuentra registrado, favor de verificar",400);
        }
    }
}
