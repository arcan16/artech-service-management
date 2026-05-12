package com.Ar_Tech.dto.clients;

import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.models.ClientEntity;
import com.Ar_Tech.models.PersonEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record FullClientDTO(
        @NotNull Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String customerCode,
        BigDecimal creditLimit) {

    public FullClientDTO(PersonEntity person, ClientEntity client) {
        this(person.getId(), person.getFirstName(), person.getLastName(), person.getEmail(), person.getPhone(),
                client.getCustomerCode(), client.getCreditLimit());
    }

    public FullClientDTO(ClientEntity client) {
        this(client.getId(), client.getPerson().getFirstName(), client.getPerson().getLastName(),
                client.getPerson().getEmail(), client.getPerson().getPhone(), client.getCustomerCode(), client.getCreditLimit());
    }
}