package com.Ar_Tech.controllers;

import com.Ar_Tech.dto.serviceOrderHistory.CreateServiceOrderHistoryDTO;
import com.Ar_Tech.dto.serviceOrderHistory.FullServiceOrderHistoryDTO;
import com.Ar_Tech.dto.serviceOrderHistory.UpdateServiceOrderHistoryDTO;
import com.Ar_Tech.services.ServiceOrderHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/service-order-history")
public class ServiceOrderHistoryController {

    @Autowired
    private ServiceOrderHistoryService serviceOrderHistoryService;

    @PostMapping
    public ResponseEntity<?> createServiceOrderHistory(@ModelAttribute @Valid CreateServiceOrderHistoryDTO serviceOrderHistoryData,
                                                       @RequestParam("imageMetadata") String imageMetadata,
                                                       HttpServletRequest request,
                                                       UriComponentsBuilder uriComponentsBuilder) throws JsonProcessingException {

        Long serviceOrderId = serviceOrderHistoryService.create(serviceOrderHistoryData, imageMetadata, request);

        URI url = uriComponentsBuilder.path("/service-order/{id}").buildAndExpand(serviceOrderId).toUri();

        return ResponseEntity.created(url).body(serviceOrderId);
    }

    /// Eliminación del registro
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request){

        serviceOrderHistoryService.delete(id, request);

        return ResponseEntity.ok().body("{\"Message\":\"Registro eliminado correctamente\"}");
    }

    /// Consulta de historial para ServiceOrder (no tiene sentido consultar todos los registros)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id, HttpServletRequest request){

        FullServiceOrderHistoryDTO serviceOrderHistory = serviceOrderHistoryService.get(id, request);

        return ResponseEntity.ok().body(serviceOrderHistory);
    }

    /// Consulta todos los ServiceOrderHistory de una ServiceOrder
    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id, HttpServletRequest request){

        List<FullServiceOrderHistoryDTO> serviceOrderHistoryDTOList = serviceOrderHistoryService.getAll(id, request);

        return ResponseEntity.ok().body(serviceOrderHistoryDTOList);
    }

    /// Actualización del registro
    @PutMapping
    public ResponseEntity<?> update(@ModelAttribute @Valid UpdateServiceOrderHistoryDTO serviceOrderHistory,
                                    @RequestParam("imageMetadata") String imageMetadata,
                                    HttpServletRequest request) throws JsonProcessingException {

        serviceOrderHistoryService.update(serviceOrderHistory, imageMetadata, request);

        return ResponseEntity.ok().body("{\"message\":\"Registro actualizado correctamente\"}");
    }
}
