package com.Ar_Tech.services;

import com.Ar_Tech.dto.clients.ClientFullDTO;
import com.Ar_Tech.dto.clients.FullClientDTO;
import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.ClientEntity;
import com.Ar_Tech.models.PersonEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.ClientRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private PersonService personService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public FullClientDTO create(ClientFullDTO clientData, HttpServletRequest request){

        PersonDTO personalData = new PersonDTO(clientData);
        PersonEntity personCreated = personService.createEntity(personalData, request);

        ClientEntity newClient = new ClientEntity(generateClientCode(personCreated), clientData, personCreated);
        clientRepository.save(newClient);

        FullClientDTO fullClientData = new FullClientDTO(personCreated, newClient);

        auditLogService.create(request, EAuditAction.INSERT, "PERSONS/CLIENTS", fullClientData.id(),null,
                new ObjectMapper().writeValueAsString(fullClientData));

        return fullClientData;
    }

    public String generateClientCode(PersonEntity person){
        return person.getFirstName().substring(0,3).toUpperCase()
                + person.getLastName().substring(0,3).toUpperCase()
                + person.getId();
    }

    public FullClientDTO getById(Long id, HttpServletRequest request){

        FullClientDTO getClient = new FullClientDTO(clientRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe, favor de verificar", 400)));

        auditLogService.create(request, EAuditAction.SELECT, "CLIENTS", getClient.id(),
                new ObjectMapper().writeValueAsString(getClient), null);

        return getClient;
    }

    public List<FullClientDTO> getAllData(Pageable page, HttpServletRequest request) {
        List<FullClientDTO> clientDTOList = clientRepository.findAll(page).map(FullClientDTO::new).toList();

        auditLogService.create(request, EAuditAction.SELECT, "CLIENTS", null,
                new ObjectMapper().writeValueAsString(clientDTOList), null);

        return clientDTOList;
    }

    public void delete(Long id, HttpServletRequest request) {

        ClientEntity clientToDelete = clientRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("Registro incorrecto, no es posible eliminar",400));

        FullClientDTO clientDeleted = new FullClientDTO(clientToDelete);

        personService.delete(id, request);

        auditLogService.create(request, EAuditAction.DELETE, "CLIENTS/PERSONS", clientToDelete.getId(),
                new ObjectMapper().writeValueAsString(clientDeleted), null);

    }

    public String update(@Valid FullClientDTO clientDTO, HttpServletRequest request) {
        ClientEntity clientToUpdate = clientRepository.findById(clientDTO.id()).
                orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe, favor de verificar", 400));

        FullPersonDTO personToUpdate = new FullPersonDTO(clientDTO);
        personService.update(personToUpdate, request);

        FullClientDTO clientToUpdateDTO = new  FullClientDTO(clientToUpdate);

        clientToUpdate.update(clientDTO);
        clientRepository.save(clientToUpdate);

        FullClientDTO updatedClient = new FullClientDTO(clientToUpdate);

        auditLogService.create(request, EAuditAction.UPDATE, "CLIENTS/PERSONS", clientToUpdateDTO.id(),
                new ObjectMapper().writeValueAsString(clientToUpdate), new ObjectMapper().writeValueAsString(updatedClient));

        return "Registro actualizado!";
    }
}
