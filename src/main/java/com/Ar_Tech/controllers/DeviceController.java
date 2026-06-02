package com.Ar_Tech.controllers;

import com.Ar_Tech.dto.device.CreateDeviceDTO;
import com.Ar_Tech.dto.device.FullDeviceDTO;
import com.Ar_Tech.dto.device.UpdateDeviceDTO;
import com.Ar_Tech.services.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<?> createDevice(@Valid @RequestBody CreateDeviceDTO deviceDTO,
                                          HttpServletRequest request,
                                          UriComponentsBuilder uriComponentsBuilder) {

        FullDeviceDTO deviceCreated = deviceService.create(deviceDTO, request);

        URI url = uriComponentsBuilder.path("/devices/{id}").buildAndExpand(deviceCreated.id()).toUri();

        return ResponseEntity.created(url).body(deviceCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDevice(@PathVariable Long id, HttpServletRequest request) {
        FullDeviceDTO deviceData = deviceService.getById(id, request);

        return ResponseEntity.ok().body(deviceData);
    }

    @GetMapping
    public ResponseEntity<?> getAllDevices(@PageableDefault(size = 10) Pageable page,
                                               HttpServletRequest request){
        List<FullDeviceDTO> allDevices =  deviceService.getAll(page, request);

        if(allDevices.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(allDevices);
    }

    @GetMapping("/")
    public ResponseEntity<?> getEmptyId(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();

        response.put("GET:/url/devices/{id}","Get the device that matches the id");
        response.put("GET:/url/devices","Get all the devices");
        response.put("POST:/url/devices","Create new Device");
        response.put("DELETE:/url/devices/{id}","Delete device that matches de id");
        response.put("UPDATE:/url/devices","Update record data");

        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long id, HttpServletRequest request) {

        deviceService.delete(id, request);

        return ResponseEntity.ok().body("{\"message\":\"Registro eliminado correctamente\"}");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody UpdateDeviceDTO deviceDTO, HttpServletRequest request){

        FullDeviceDTO deviceUpdated = deviceService.update(deviceDTO, request);

        return ResponseEntity.ok().build();
    }
}
