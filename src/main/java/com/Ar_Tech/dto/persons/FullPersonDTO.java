package com.Ar_Tech.dto.persons;

import com.Ar_Tech.models.PersonEntity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FullPersonDTO(@NotNull Long id,
                            String firstName,
                            String lastName,
                            String email,
                            String phone,
                            LocalDateTime createdAt) {
    public FullPersonDTO(PersonEntity newPerson) {
        this(newPerson.getId(), newPerson.getFirstName(), newPerson.getLastName(), newPerson.getEmail(),
                newPerson.getPhone(), newPerson.getCreatedAt());
    }
}
