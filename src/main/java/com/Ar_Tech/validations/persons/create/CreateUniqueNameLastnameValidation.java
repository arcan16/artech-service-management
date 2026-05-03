package com.Ar_Tech.validations.persons.create;

import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.PersonEntity;
import com.Ar_Tech.repositories.PersonRepository;
import com.Ar_Tech.validations.persons.create.IPersonValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("createPersonValidators")
public class CreateUniqueNameLastnameValidation implements IPersonValidation {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void validate(PersonDTO personDTO) {
        List<PersonEntity> isPerson = personRepository.findByFirstNameAndLastName(personDTO.firstName(), personDTO.lastName());
        if(!isPerson.isEmpty()){
            throw new MyIntegrityValidation("El nombre y apellido ya se encuentra registrado",400);
        }
    }
}
