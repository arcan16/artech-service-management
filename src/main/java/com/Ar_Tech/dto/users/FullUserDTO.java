package com.Ar_Tech.dto.users;

import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record FullUserDTO(@NotNull Long id,
                          String firstName,
                          String lastName,
                          String email,
                          String phone,
                          String username,
                          EUserRole role) {
    public FullUserDTO(UserEntity user) {
        this(user.getId(), user.getPerson().getFirstName(), user.getPerson().getLastName(), user.getPerson().getEmail(),
                user.getPerson().getPhone(), user.getUsername(), user.getRole());
    }
}
