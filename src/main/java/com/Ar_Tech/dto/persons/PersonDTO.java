package com.Ar_Tech.dto.persons;

import com.Ar_Tech.dto.clients.ClientFullDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public record PersonDTO(@NonNull
                        @NotBlank(message = "First name is required")
                        @Size(max = 50, message = "First name must not exceed 50 characters")
                        String firstName,

                        @NonNull
                        @NotBlank(message = "Last name is required")
                        @Size(max = 50, message = "Last name must not exceed 50 characters")
                        String lastName,

                        @NonNull
                        @NotBlank(message = "Email is required")
                        @Email(message = "Email must be valid")
                        @Size(max = 150, message = "Email must not exceed 150 characters")
                        String email,

                        @NonNull
                        @NotBlank(message = "Phone is required")
                        String phone,

                        LocalDateTime createdAt) {
    public PersonDTO(ClientFullDTO clientData) {
        this(clientData.firstName(), clientData.lastName(), clientData.email(), clientData.phone(), null);
    }
}
