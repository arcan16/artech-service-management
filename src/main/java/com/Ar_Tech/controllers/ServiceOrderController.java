package com.Ar_Tech.controllers;

import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import com.Ar_Tech.dto.serviceOrder.FullServiceOrderDTO;
import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.dto.serviceOrderImage.FullServiceOrderImageDTO;
import com.Ar_Tech.services.ServiceOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/service-order")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService servOrderService;

    /// Debo re-factorizar el código
    /// Crea una nueva orden de servicio
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute @Valid CreateServiceOrderDTO serviceOrderDTO,
                                    @RequestPart("imageMetadata") String  imageMetadata,
                                    HttpServletRequest request,
                                    UriComponentsBuilder uriComponentsBuilder) throws JsonProcessingException {

        Long idRecord = servOrderService.create(serviceOrderDTO, imageMetadata, request);

        URI url = uriComponentsBuilder.path("/service-order/{id}").buildAndExpand(idRecord).toUri();

        return ResponseEntity.created(url).body(idRecord);
    }

    /// Responde con todos los registros almacenados en la BDD
    @GetMapping
    public ResponseEntity<?> getAllServiceOrders(@PageableDefault(size = 10) Pageable page,
                                                 HttpServletRequest request){

        List<FullServiceOrderDTO> serviceOrders = servOrderService.getAll(page, request);

        if(serviceOrders.isEmpty())
            return ResponseEntity.ok().body("{\"message\":\"No existen ordenes de servicio registradas\"}");

        return ResponseEntity.ok().body(serviceOrders);
    }

    /// Obtiene la información del registro indicado
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        FullServiceOrderDTO dataServiceOrder =  servOrderService.getById(id);
        return ResponseEntity.ok().body(dataServiceOrder);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<?> getAllImagesOfServiceOrder(@PathVariable Long id,
                                                        HttpServletRequest request){

        List<FullServiceOrderImageDTO> images = servOrderService.getImagesOfServiceOrder(id, request);

        return ResponseEntity.ok().body(images);
    }

    // Elimina registro de la BDD
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteServiceOrder(@PathVariable Long id,
                                                HttpServletRequest request){

        servOrderService.delete(id, request);

        return ResponseEntity.ok().body("{\"message\":\"El registro fue eliminado correctamente\"}");
    }

    // Actualiza la información en la BDD
    @PutMapping
    public ResponseEntity<?> update(@ModelAttribute @Valid UpdateServiceOrderDTO serviceOrder,
                                    @RequestPart("imageMetadata") String  imageMetadata,
                                    HttpServletRequest request) throws JsonProcessingException {
        servOrderService.update(serviceOrder, imageMetadata, request);

        return ResponseEntity.ok().body("{\"message\":\"Registro actualizado correctamente\"}");
    }

    /**
     * Agregar registro de bitácora a cada transacción realizada en la base de datos
     */
}