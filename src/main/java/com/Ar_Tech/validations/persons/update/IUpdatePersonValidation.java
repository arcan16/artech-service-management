package com.Ar_Tech.validations.persons.update;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;

public interface IUpdatePersonValidation {

    void validate(FullPersonDTO personDTO);
}
