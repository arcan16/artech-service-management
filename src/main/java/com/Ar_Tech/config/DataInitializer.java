package com.Ar_Tech.config;

import com.Ar_Tech.models.PersonEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EUserRole;
import com.Ar_Tech.models.enums.EUserStatus;
import com.Ar_Tech.repositories.PersonRepository;
import com.Ar_Tech.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * Inicializa datos esenciales al arranque de la aplicacion
 *
 * Este componente crea un usuario administrador por defecto
 * en caso de que no existan usuarios registrados en la base de datos
 *
 * IMPORTANTE:
 * - Solo debe ejecutarse en entornos controlados.
 * - Se recomienda cambiar la contraseña en el primer login.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PersonRepository personRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0 && personRepository.count() == 0){
            PersonEntity person = new PersonEntity();
            person.setFirstName("Jhon");
            person.setLastName("Doe");
            person.setPhone("9999999999");

            personRepository.save(person);

            UserEntity admin = new UserEntity();
            admin.setPerson(person);
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(EUserRole.ADMIN);
            admin.setStatus(EUserStatus.ACTIVE);

            userRepository.save(admin);
        }
    }
}
