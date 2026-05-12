package com.Ar_Tech.services;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.AuditLogEntity;
import com.Ar_Tech.models.PersonEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.PersonRepository;
import com.Ar_Tech.repositories.UserRepository;
import com.Ar_Tech.validations.persons.create.IPersonValidation;
import com.Ar_Tech.validations.persons.update.IUpdatePersonValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    @Qualifier("createPersonValidators")
    private List<IPersonValidation> createValidators = new ArrayList<>();

    @Autowired
    private List<IUpdatePersonValidation> updateValidators = new ArrayList<>();

    public FullPersonDTO create(PersonDTO personDTO, HttpServletRequest request){
        return new FullPersonDTO(createEntity(personDTO, request));
    }

    public PersonEntity createEntity(PersonDTO personDTO, HttpServletRequest request){
        createValidators.forEach(i->i.validate(personDTO));

        PersonEntity newPerson = new PersonEntity(personDTO);

        personRepository.save(newPerson);


        auditLogService.create(request, EAuditAction.INSERT, "PERSONS", newPerson.getId(), null,
                new ObjectMapper().writeValueAsString(newPerson));

        return newPerson;
    }



    public void delete(Long id, HttpServletRequest request){
        UserEntity author = jwtUtils.getUserFromRequest(request);

        if(author.getId().equals(id)){
            throw new MyIntegrityValidation("No tienes autorizado eliminar tu propio registro", 400);
        }

        PersonEntity personToDelete = personRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe",400));

        personRepository.delete(personToDelete);

        auditLogService.create(request, EAuditAction.DELETE, "PERSONS", personToDelete.getId(),
                new ObjectMapper().writeValueAsString(personToDelete), null);
    }

    public String update(@Valid FullPersonDTO fullPersonDTO, HttpServletRequest request) {
        updateValidators.forEach(i-> i.validate(fullPersonDTO));

        PersonEntity personToUpdate = personRepository.findById(fullPersonDTO.id())
                .orElseThrow(()->new MyIntegrityValidation("El registro indicado no existe",400));

        if(!isUpdatable(personToUpdate, fullPersonDTO)){
            return "La actualizacion no es necesaria, los campos recibidos son iguales a los del registro";
        }

        FullPersonDTO oldValues = new FullPersonDTO(personToUpdate);

        personToUpdate.update(fullPersonDTO);
        personRepository.save(personToUpdate);

        auditLogService.create(request, EAuditAction.UPDATE, "PERSONS", personToUpdate.getId(),
                new ObjectMapper().writeValueAsString(oldValues), new ObjectMapper().writeValueAsString(personToUpdate));

        return "Registro actualizado con exito!";
    }

    public boolean isUpdatable(PersonEntity person, FullPersonDTO personDTO){
        int count = 0;

        if(!personDTO.firstName().isBlank() && !personDTO.firstName().equals(person.getFirstName())){
            count ++;
        }else if(!personDTO.lastName().isBlank() && !personDTO.lastName().equals(person.getLastName())){
            count ++;
        }else if(!personDTO.email().isBlank() && !personDTO.email().equals(person.getEmail())){
            count ++;
        }else if(!personDTO.phone().isBlank() && !personDTO.phone().equals(person.getPhone())){
            count ++;
        }
        return count > 0;
    }

    public List<FullPersonDTO> getAllData(Pageable page, HttpServletRequest request) {

        List<FullPersonDTO> personsList = personRepository.findAll(page).map(FullPersonDTO::new).toList();

        auditLogService.create(request, EAuditAction.SELECT, "PERSONS", null,
                new ObjectMapper().writeValueAsString(personsList), null);

        return personsList;
    }

    public FullPersonDTO getSingleData(Long id, HttpServletRequest request) {
        FullPersonDTO selectPerson =  new FullPersonDTO(personRepository.findById(id)
                .orElseThrow(()->new MyIntegrityValidation("El registro indicado no existe",400)));

        auditLogService.create(request, EAuditAction.SELECT, "PERSONS", null,
                new ObjectMapper().writeValueAsString(selectPerson), null);

        return selectPerson;
    }
}
