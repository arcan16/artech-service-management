package com.Ar_Tech.dto.users;

import com.Ar_Tech.models.enums.EUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record UserDTO(@NonNull
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

                      @NonNull
                      @NotBlank(message = "Username is required")
                      @Size(max = 50, message = "Username must not exceed 50 characters")
                      String username,

                      @NonNull
                      @NotBlank(message = "Password is required")
                      @Size(max = 50, message = "Password must not exceed 50 characters")
                      String password,

                      @NonNull
                      EUserRole role
                      ) {
}
