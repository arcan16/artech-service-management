package com.Ar_Tech.services;

import com.Ar_Tech.dto.customerDevice.CreateCustomerDeviceDTO;
import com.Ar_Tech.dto.customerDevice.FullCustomerDeviceDTO;
import com.Ar_Tech.dto.customerDevice.UpdateCustomerDeviceDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ClientEntity;
import com.Ar_Tech.models.CustomerDeviceEntity;
import com.Ar_Tech.models.DeviceEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.ClientRepository;
import com.Ar_Tech.repositories.CustomerDeviceRepository;
import com.Ar_Tech.repositories.DeviceRepository;
import com.Ar_Tech.validations.customerDevice.create.ICreateCustomerDeviceValidation;
import com.Ar_Tech.validations.customerDevice.update.IUpdateCustomerDevieValidation;
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
public class CustomerDeviceService {
    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;

    @Autowired
    private List<ICreateCustomerDeviceValidation> createValidation = new ArrayList<>();

    @Autowired
    List<IUpdateCustomerDevieValidation> updateValidation = new ArrayList<>();

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public FullCustomerDeviceDTO create(@Valid CreateCustomerDeviceDTO customerDeviceDTO, HttpServletRequest request) {
        createValidation.forEach(v-> v.validate(customerDeviceDTO, request));
        ClientEntity client = clientRepository.getReferenceById(customerDeviceDTO.client());
        DeviceEntity device = deviceRepository.getReferenceById(customerDeviceDTO.device());
        CustomerDeviceEntity newCustomerDevice = new CustomerDeviceEntity(client, device, customerDeviceDTO);

        customerDeviceRepository.save(newCustomerDevice);

        auditLogService.create(request, EAuditAction.INSERT, "CUSTOMER_DEVICES", newCustomerDevice.getId(), null,
                new ObjectMapper().writeValueAsString(newCustomerDevice));

        return new FullCustomerDeviceDTO(newCustomerDevice);
    }

    public FullCustomerDeviceDTO get(Long id, HttpServletRequest request) {
        CustomerDeviceEntity customerDevice = customerDeviceRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("Error: El registro indicado no existe",400));

        auditLogService.create(request, EAuditAction.SELECT, "CUSTOMER_DEVICES", customerDevice.getId(),
                new ObjectMapper().writeValueAsString(customerDevice),null);

        return new FullCustomerDeviceDTO(customerDevice);
    }

    public List<FullCustomerDeviceDTO> getAll(Pageable page, HttpServletRequest request) {
        List<FullCustomerDeviceDTO> dataList = customerDeviceRepository.findAll().stream().map(FullCustomerDeviceDTO::new).toList();

        auditLogService.create(request, EAuditAction.SELECT, "CUSTOMER_DEVICES", null,
                new ObjectMapper().writeValueAsString(dataList),null);

        return dataList;
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request) {
        CustomerDeviceEntity cdToDelete= customerDeviceRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("El regisrto indicado no existe", 400));

        auditLogService.create(request, EAuditAction.DELETE, "CUSTOMER_DEVICES", id,
                new ObjectMapper().writeValueAsString(cdToDelete),null);

        customerDeviceRepository.delete(cdToDelete);
    }

    @Transactional
    public FullCustomerDeviceDTO update(@Valid UpdateCustomerDeviceDTO customerDeviceDTO, HttpServletRequest request) {

        CustomerDeviceEntity customerDevice = customerDeviceRepository.findById(customerDeviceDTO.id())
                .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe",400));

        ClientEntity client = clientRepository.findById(customerDeviceDTO.client())
                        .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe",400));

        DeviceEntity device = deviceRepository.findById(customerDeviceDTO.device())
                        .orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe", 400));


        updateValidation.forEach(v->v.validate(customerDeviceDTO, request));

        FullCustomerDeviceDTO oldData = new FullCustomerDeviceDTO(customerDevice);

        customerDevice.update(customerDeviceDTO, client, device);
        customerDeviceRepository.save(customerDevice);

        auditLogService.create(request, EAuditAction.UPDATE, "CUSTOMER DEVICE", customerDevice.getId(),
                new ObjectMapper().writeValueAsString(oldData), new ObjectMapper().writeValueAsString(customerDevice));

        return new FullCustomerDeviceDTO(customerDevice);
    }
}
