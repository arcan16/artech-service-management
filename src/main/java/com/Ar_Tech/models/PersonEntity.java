package com.Ar_Tech.models;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public PersonEntity(PersonDTO personDTO) {
        this.firstName = personDTO.firstName();
        this.lastName = personDTO.lastName();
        this.email = personDTO.email();
        this.phone = personDTO.phone();
    }

    public void update(@Valid FullPersonDTO person) {
        if(this.firstName != null)
            this.firstName = person.firstName();

        if(this.lastName != null)
            this.lastName = person.lastName();

        if(this.email != null)
            this.email = person.email();

        if(this.phone != null)
            this.phone = person.phone();
    }
}