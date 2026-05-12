package com.Ar_Tech.controllers;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.dto.persons.PersonDTO;
import com.Ar_Tech.services.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid PersonDTO personDTO,
                                    UriComponentsBuilder uriComponentsBuilder,
                                    HttpServletRequest request) {

        FullPersonDTO personCreated = personService.create(personDTO, request);

        URI url = uriComponentsBuilder.path("/persons/{id}").buildAndExpand(personCreated.id()).toUri();

        return ResponseEntity.created(url).body(personCreated);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid FullPersonDTO fullPersonDTO,
                                    HttpServletRequest request) {

        String message = personService.update(fullPersonDTO, request);

        return ResponseEntity.ok().body("{\"message\":\""+message+"\"}");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request){
        personService.delete(id, request);

        return ResponseEntity.ok().body("{\"message\":\"Registro eliminado correctamente\"}");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(HttpServletRequest request, @PageableDefault(size = 10) Pageable page){

        List<FullPersonDTO> personDTOList = personService.getAllData(page, request);

        if(personDTOList.isEmpty())
            return ResponseEntity.ok().body("{\"message\":\"Sin registros encontrados\"}");

        return ResponseEntity.ok().body(personDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request){
        FullPersonDTO personData = personService.getSingleData(id, request);

        return ResponseEntity.ok().body(personData);
    }
}
