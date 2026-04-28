package com.Ar_Tech.repositories;

import com.Ar_Tech.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
    
    Optional<Person> findByPhone(String phone);
    
    Page<Person> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
    
    Page<Person> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}