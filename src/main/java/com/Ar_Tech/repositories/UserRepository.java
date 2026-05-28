
package com.Ar_Tech.repositories;

import com.Ar_Tech.models.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(@NonNull @NotBlank(message = "Username is required") @Size(max = 50, message = "Username must not exceed 50 characters") String username);

    Boolean existsByUsernameAndIdNot(@NonNull @NotBlank(message = "Username is required")String username, @NotNull Long id);
}
