package com.Ar_Tech.controllers;

import com.Ar_Tech.dto.clients.ClientFullDTO;
import com.Ar_Tech.dto.clients.FullClientDTO;
import com.Ar_Tech.services.ClientService;
import com.Ar_Tech.services.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PersonService personService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TECHNICIAN')")
    public ResponseEntity<?> create(@Valid @RequestBody ClientFullDTO clientDTO,
                                    HttpServletRequest request,
                                    UriComponentsBuilder uriComponentsBuilder) {
        FullClientDTO clientCreated = clientService.create(clientDTO, request);

        URI url = uriComponentsBuilder.path("/clients/{id}").buildAndExpand(clientCreated.id()).toUri();

        return ResponseEntity.created(url).body(clientCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
        FullClientDTO clientData = clientService.getById(id, request);

        return ResponseEntity.ok(clientData);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10)Pageable page, HttpServletRequest request){

        List<FullClientDTO> clientDTOList = clientService.getAllData(page, request);

        if(clientDTOList.isEmpty())
            return ResponseEntity.ok().body("{\"message\":\"Sin registros encontrados\"}");

        return ResponseEntity.ok(clientDTOList);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TECHNICIAN')")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        clientService.delete(id,request);

        return ResponseEntity.ok().body("{\"message\":\"Registro eliminado correctamente\"}");
    }


    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody FullClientDTO clientDTO,
                                            HttpServletRequest request) {
        String message = clientService.update(clientDTO, request);

        return ResponseEntity.ok().body("{\"message\":\""+message+"\"}");
    }


}