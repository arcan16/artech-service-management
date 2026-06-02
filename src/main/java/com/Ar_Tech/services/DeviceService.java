package com.Ar_Tech.services;

import com.Ar_Tech.dto.device.CreateDeviceDTO;
import com.Ar_Tech.dto.device.FullDeviceDTO;
import com.Ar_Tech.dto.device.UpdateDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.DeviceEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.DeviceRepository;
import com.Ar_Tech.validations.devices.create.ICreateDeviceValidation;
import com.Ar_Tech.validations.devices.update.IUpdateDeviceValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private List<ICreateDeviceValidation> validations = new ArrayList<>();

    @Autowired
    private List<IUpdateDeviceValidation> updateValidations = new ArrayList<>();

    @Autowired
    private AuditLogService  auditLogService;

    @Transactional
    public FullDeviceDTO create(@Valid CreateDeviceDTO deviceDTO, HttpServletRequest request) {
        validations.forEach(v-> v.validate(deviceDTO));

        DeviceEntity newDevice = new DeviceEntity(deviceDTO);
        deviceRepository.save(newDevice);

        auditLogService.create(request, EAuditAction.INSERT, "DEVICES", newDevice.getId(), null,
                new ObjectMapper().writeValueAsString(newDevice));

        return new FullDeviceDTO(newDevice);
    }

    public FullDeviceDTO getById(Long id, HttpServletRequest request) {
        DeviceEntity device = deviceRepository.findById(id).
                orElseThrow(()-> new MyIntegrityValidation("Error, el registro indicado no existe", 400));

        return new FullDeviceDTO(device);
    }


    public void delete(Long id, HttpServletRequest request) {
        DeviceEntity device = deviceRepository.findById(id).
                orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe", 400));

        FullDeviceDTO oldData = new FullDeviceDTO(device);

        deviceRepository.delete(device);

        auditLogService.create(request, EAuditAction.DELETE, "DEVICES", oldData.id(),
                new ObjectMapper().writeValueAsString(oldData), null);
    }

    @Transactional
    public FullDeviceDTO update(@Valid UpdateDeviceDTO deviceDTO, HttpServletRequest request) {
        DeviceEntity deviceToUpdate = deviceRepository.findById(deviceDTO.id())
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no exite", 400));

        updateValidations.forEach(v-> v.validate(deviceDTO, request, deviceToUpdate));

        FullDeviceDTO oldData = new FullDeviceDTO(deviceToUpdate);

        deviceToUpdate.update(deviceDTO);
        deviceRepository.save(deviceToUpdate);

    auditLogService.create(request, EAuditAction.UPDATE, "DEVICES", deviceToUpdate.getId(),
            new ObjectMapper().writeValueAsString(oldData), new ObjectMapper().writeValueAsString(deviceToUpdate));

        return new FullDeviceDTO(deviceToUpdate);
    }

    public List<FullDeviceDTO> getAll(Pageable page, HttpServletRequest request) {
        return deviceRepository.findAll(page).map(FullDeviceDTO::new).toList();
    }
}
