package com.Ar_Tech.controllers;

import com.Ar_Tech.dto.users.FullUserDTO;
import com.Ar_Tech.dto.users.UserDTO;
import com.Ar_Tech.dto.users.UserFullDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid UserDTO userDTO,
                                    HttpServletRequest request,
                                    UriComponentsBuilder uriComponentsBuilder) {

        FullUserDTO userCreated = userService.create(userDTO, request);

        URI url = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userCreated.id()).toUri();

        return ResponseEntity.created(url).body(userCreated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSingleUser(@PathVariable Long id, HttpServletRequest request) {
        FullUserDTO singleUserData = userService.getSingle(id, request);

        return ResponseEntity.ok().body(singleUserData);
    }

    @GetMapping
    public ResponseEntity<?> getSelfData(HttpServletRequest request) {
        FullUserDTO selfData = userService.getSelfData(request);

        return ResponseEntity.ok().body(selfData);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(@PageableDefault(size = 10)Pageable page, HttpServletRequest request) {
        List<FullUserDTO> allUsersData = userService.getAllUsersData(page, request);

        if(allUsersData.isEmpty())
            throw new MyIntegrityValidation("No se encontraron registros", 400);

        return ResponseEntity.ok().body(allUsersData);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {

        userService.delete(id, request);

        return ResponseEntity.ok().body("{\"message\":\"El usuario con id: "+ id +" fue con exito!\"}");
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid UserFullDTO userDTO, HttpServletRequest request) {
        ObjectNode userUpdated = userService.update(userDTO, request);

        return ResponseEntity.ok().body(userUpdated);
    }
}
