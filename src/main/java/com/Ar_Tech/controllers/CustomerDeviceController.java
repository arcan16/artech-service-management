package com.Ar_Tech.controllers;

import com.Ar_Tech.dto.customerDevice.CreateCustomerDeviceDTO;
import com.Ar_Tech.dto.customerDevice.FullCustomerDeviceDTO;
import com.Ar_Tech.dto.customerDevice.UpdateCustomerDeviceDTO;
import com.Ar_Tech.services.CustomerDeviceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("customerDevice")
public class CustomerDeviceController {

    @Autowired
    private CustomerDeviceService customerDeviceService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateCustomerDeviceDTO customerDeviceDTO, HttpServletRequest request,
                                    UriComponentsBuilder uriComponentsBuilder){

        FullCustomerDeviceDTO customerDeviceCreated = customerDeviceService.create(customerDeviceDTO, request);
        URI url = uriComponentsBuilder.path("/customerDevice").buildAndExpand(customerDeviceCreated.id()).toUri();

        return ResponseEntity.created(url).body(customerDeviceCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id, HttpServletRequest request){

        FullCustomerDeviceDTO customerDevice = customerDeviceService.get(id, request);

        return ResponseEntity.ok().body(customerDevice);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10)Pageable page, HttpServletRequest request){
        List<FullCustomerDeviceDTO> dataList = customerDeviceService.getAll(page, request);

        if(dataList.isEmpty()){
            return ResponseEntity.ok().body("{\"message\":\"No existen registros\"}");
        }

        return ResponseEntity.ok().body(dataList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request){

        customerDeviceService.delete(id, request);

        return ResponseEntity.ok().body("{\"message\":\"Registro eliminado correctamente\"}");
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid UpdateCustomerDeviceDTO customerDeviceDTO,
                                    HttpServletRequest request){

        FullCustomerDeviceDTO cdUpdated = customerDeviceService.update(customerDeviceDTO, request);

        return ResponseEntity.ok().body(cdUpdated);
    }
}
