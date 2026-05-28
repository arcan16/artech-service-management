package com.Ar_Tech.dto.users;

import com.Ar_Tech.models.enums.EUserRole;
import com.Ar_Tech.models.enums.EUserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record UserFullDTO(@NotNull Long id,
                          String firstName,
                          String lastName,
                          @Email(message = "Email must be valid")
                          @Size(max = 150, message = "Email must not exceed 150 characters")
                          String email,
                          String phone,
                          String username,
                          String password,
                          EUserRole role,
                          EUserStatus status
                      ) {
}
