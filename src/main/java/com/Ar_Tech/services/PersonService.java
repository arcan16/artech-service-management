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
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        createValidators.forEach(i->i.validate(personDTO));

        UserEntity author = jwtUtils.getUserFromRequest(request);

        PersonEntity newPerson = new PersonEntity(personDTO);

        personRepository.save(newPerson);

        AuditLogEntity auditLog = new AuditLogEntity(author, EAuditAction.INSERT, "persons", newPerson);

        auditLogService.create(auditLog);

        return new FullPersonDTO(newPerson);
    }


    public void delete(Long id, HttpServletRequest request){
        UserEntity author = jwtUtils.getUserFromRequest(request);

        if(author.getId().equals(id)){
            throw new MyIntegrityValidation("No tienes autorizado eliminar tu propio registro", 400);
        }

        PersonEntity personToDelete = personRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe",400));

        personRepository.delete(personToDelete);

        AuditLogEntity auditLog = new AuditLogEntity(author, EAuditAction.DELETE, "persons", personToDelete);
        auditLogService.create(auditLog);
    }

    public String update(@Valid FullPersonDTO fullPersonDTO, HttpServletRequest request) {
        updateValidators.forEach(i-> i.validate(fullPersonDTO));

        UserEntity author = jwtUtils.getUserFromRequest(request);
        PersonEntity personToUpdate = personRepository.findById(fullPersonDTO.id())
                .orElseThrow(()->new MyIntegrityValidation("El registro indicado no existe",400));

        if(!isUpdatable(personToUpdate, fullPersonDTO)){
            return "La actualizacion no es necesaria, los campos recibidos son iguales a los del registro";
        }

        FullPersonDTO oldValues = new FullPersonDTO(personToUpdate);

        personToUpdate.update(fullPersonDTO);
        personRepository.save(personToUpdate);

        AuditLogEntity auditLog = new AuditLogEntity(author, EAuditAction.UPDATE, "persons", oldValues, personToUpdate );
        auditLogService.create(auditLog);

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

        UserEntity author = jwtUtils.getUserFromRequest(request);
        AuditLogEntity auditLog = new AuditLogEntity(author, EAuditAction.SELECT, "persons", personsList);
        auditLogService.create(auditLog);

        return personsList;
    }

    public FullPersonDTO getSingleData(Long id, HttpServletRequest request) {
        FullPersonDTO selectPerson =  new FullPersonDTO(personRepository.findById(id)
                .orElseThrow(()->new MyIntegrityValidation("El registro indicado no existe",400)));

        UserEntity author =  jwtUtils.getUserFromRequest(request);

        AuditLogEntity auditLog = new AuditLogEntity(author, EAuditAction.SELECT, "persons", selectPerson);
        auditLogService.create(auditLog);

        return selectPerson;
    }
}
