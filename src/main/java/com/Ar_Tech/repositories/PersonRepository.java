package com.Ar_Tech.repositories;

import com.Ar_Tech.models.PersonEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    Optional<PersonEntity> findByEmail(String email);
    
    Optional<PersonEntity> findByPhone(String phone);
    
    Page<PersonEntity> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
    
    Page<PersonEntity> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    List<PersonEntity> findByFirstNameAndLastName(@NonNull @NotBlank(message = "First name is required") @Size(max = 50, message = "First name must not exceed 50 characters") String s, @NonNull @NotBlank(message = "Last name is required") @Size(max = 50, message = "Last name must not exceed 50 characters") String string);

    Boolean existsByEmail(@NonNull @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 150, message = "Email must not exceed 150 characters") String email);

    Boolean findByFirstNameAndLastNameAndIdNot(@NonNull @NotBlank(message = "First name is required") String firstName, @NonNull @NotBlank(message = "Last name is required") String lastName, @NotNull Long id);

    Boolean existsByEmailAndIdNot(String email, @NotNull Long id);

    boolean existsByFirstNameAndLastNameAndIdNot(String firstName, String lastName, @NotNull Long id);
}