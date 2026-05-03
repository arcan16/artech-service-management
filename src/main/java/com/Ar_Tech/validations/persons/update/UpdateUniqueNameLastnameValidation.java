package com.Ar_Tech.validations.persons.update;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.PersonEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("updatePersonValidators")
public class UpdateUniqueNameLastnameValidation implements IUpdatePersonValidation {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void validate(FullPersonDTO personDTO) {
        PersonEntity personToUpdate = personRepository.findById(personDTO.id())
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe, favor de verificar",400));

        String firstName = personDTO.firstName() != null
                ? personDTO.firstName()
                : personToUpdate.getFirstName();

        String lastName = personDTO.lastName() != null
                ? personDTO.lastName()
                : personToUpdate.getLastName();

        boolean exists = personRepository.existsByFirstNameAndLastNameAndIdNot(
                firstName,
                lastName,
                personDTO.id()
        );

        if (exists) {
            throw new MyIntegrityValidation("La persona ya existe", 400);
        }
    }
}
