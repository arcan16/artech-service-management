package com.Ar_Tech.services;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.dto.users.FullUserDTO;
import com.Ar_Tech.dto.users.UserDTO;
import com.Ar_Tech.dto.users.UserFullDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.PersonEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.PersonRepository;
import com.Ar_Tech.repositories.UserRepository;
import com.Ar_Tech.validations.users.create.IUserValidation;
import com.Ar_Tech.validations.users.update.IUpdateUserValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private List<IUserValidation> validation = new ArrayList<>();

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private List<IUpdateUserValidation> validationUpdate = new ArrayList<>();

    @Transactional
    public FullUserDTO create(@Valid UserDTO userDTO, HttpServletRequest request) {
        PersonDTO personData = new PersonDTO(userDTO);
        PersonEntity personCreated = personService.createEntity(personData, request);

        validation.forEach(v-> v.validate(userDTO, request));

        UserEntity userCreated = new UserEntity(personCreated, userDTO, passwordEncoder.encode(userDTO.password()));
        userRepository.save(userCreated);

        auditLogService.create(request, EAuditAction.INSERT,"USERS",personCreated.getId(),null,
                new ObjectMapper().writeValueAsString(userCreated));

        return new FullUserDTO(userCreated);
    }

    public FullUserDTO getSingle(Long id, HttpServletRequest request) {
        UserEntity singleUser = userRepository.findById(id).
                orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe", 400));

        auditLogService.create(request, EAuditAction.SELECT,"USERS/PERSONS",singleUser.getId(),
                new ObjectMapper().writeValueAsString(singleUser), null);

        return new FullUserDTO(singleUser);
    }

    public FullUserDTO getSelfData(HttpServletRequest request) {
        UserEntity selfData = jwtUtils.getUserFromRequest(request);

        auditLogService.create(request, EAuditAction.SELECT,"USERS/PERSONS",selfData.getId(),
                new ObjectMapper().writeValueAsString(selfData), null);

        return new FullUserDTO(selfData);
    }

    public List<FullUserDTO> getAllUsersData(Pageable page, HttpServletRequest request) {
        List<FullUserDTO> allUsersData = userRepository.findAll(page).stream().map(FullUserDTO::new).toList();

        auditLogService.create(request, EAuditAction.SELECT,"USERS/PERSONS",null,
                new ObjectMapper().writeValueAsString(allUsersData), null);

        return allUsersData;
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request) {
        UserEntity author = jwtUtils.getUserFromRequest(request);
        UserEntity userToDelete = userRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe",400));

        if (author.equals(userToDelete))
            throw new MyIntegrityValidation("No es posible eliminar tu propia cuenta", 400);

        auditLogService.create(request, EAuditAction.DELETE,"USERS/PERSONS",userToDelete.getId(),
                new ObjectMapper().writeValueAsString(userToDelete), null);

        personRepository.delete(userToDelete.getPerson());
    }

    public ObjectNode update(@Valid UserFullDTO userDTO, HttpServletRequest request) {

        validationUpdate.forEach(v-> v.validate(userDTO, request));

        UserEntity author = jwtUtils.getUserFromRequest(request);

        UserEntity userToUpdate = userRepository.findById(userDTO.id())
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe",400));

        FullUserDTO oldData = new FullUserDTO(userToUpdate);

        // Actualización de persons
        FullPersonDTO personDTO = new FullPersonDTO(userDTO);
        personService.update(personDTO, request);
        // Actualización de users
        userToUpdate.update(userDTO, passwordEncoder);
        userRepository.save(userToUpdate);

        ObjectMapper objectMapper = new  ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        /// Si actualiza su propio nombre de usuario debe generar un token
        if(author.getId().equals(userToUpdate.getId())){
            auditLogService.create(author, EAuditAction.SELECT,"USERS",userToUpdate.getId(),
                    new ObjectMapper().writeValueAsString(oldData), new ObjectMapper().writeValueAsString(userToUpdate));
            if(!Objects.equals(oldData.username(), author.getUsername())){
                response.put("newToken", jwtUtils.generateAccessToken(userDTO.username()));
            }
        }else{
            auditLogService.create(request, EAuditAction.SELECT,"USERS",userToUpdate.getId(),
                    new ObjectMapper().writeValueAsString(oldData), new ObjectMapper().writeValueAsString(userToUpdate));
        }

        response.set("data",  objectMapper.valueToTree(new FullUserDTO(userToUpdate)));
        auditLogService.create(request, EAuditAction.SELECT,"USERS/PERSONS",userDTO.id(),
                new ObjectMapper().writeValueAsString(userDTO), new ObjectMapper().writeValueAsString(userToUpdate));
        return response;
    }
}
