package com.Ar_Tech.services;

import com.Ar_Tech.dto.serviceOrderHistory.CreateServiceOrderHistoryDTO;
import com.Ar_Tech.dto.serviceOrderHistory.FullServiceOrderHistoryDTO;
import com.Ar_Tech.dto.serviceOrderHistory.UpdateServiceOrderHistoryDTO;
import com.Ar_Tech.dto.serviceOrderImage.CreateServiceOrderImageMetaDataDTO;
import com.Ar_Tech.dto.serviceOrderImage.ImageWithMetadataDTO;
import com.Ar_Tech.dto.serviceOrderImage.UpdateServiceOrderImageMetaDataDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.ServiceOrderHistoryEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.ServiceOrderHistoryRepository;
import com.Ar_Tech.repositories.ServiceOrderRepository;
import com.Ar_Tech.validations.serviceOrderHistory.IServiceOrderHistoryValidations;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderHistoryService {

    @Autowired
    private ServiceOrderHistoryRepository serviceOrderHistoryRepository;

    @Autowired
    private List<IServiceOrderHistoryValidations> validations = new ArrayList<>();

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ServiceOrderImageService serviceOrderImageService;

    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public Long create(@Valid CreateServiceOrderHistoryDTO serviceOrderHistoryData, String imageMetadata, HttpServletRequest request) throws JsonProcessingException {
        /// Obtener Autor
        UserEntity author = jwtUtils.getUserFromRequest(request);

        /// Obtenemos el ServiceOrderEntity
        ServiceOrderEntity serviceOrder = serviceOrderRepository.findById(serviceOrderHistoryData.serviceOrderId())
                .orElseThrow(()-> new MyIntegrityValidation("Error: La orden de servicio indicada no existe",400));

        /// Validaciones
        validations.forEach(v-> v.validate(serviceOrderHistoryData.status(), author, serviceOrder));

        /// Des-serializa los metadatos de las imágenes y lo convertimos el JSON de metadata en un objeto manipulable
        List<CreateServiceOrderImageMetaDataDTO> imagesMetaData = imageService.readJsonData(imageMetadata, CreateServiceOrderImageMetaDataDTO.class);

        /// Valída la información y crear lista con las imágenes del registro
        List<ImageWithMetadataDTO> imageWithDataList = imageService.validateAndCreateImageWithDataList(serviceOrderHistoryData.images(), imagesMetaData);

        /// Formamos el objeto para crear un nuevo ServiceOrderHistory y guardamos el registro
        ServiceOrderHistoryEntity newServiceOrderHistory =  new ServiceOrderHistoryEntity(serviceOrder, serviceOrderHistoryData,
                author);

        serviceOrderHistoryRepository.save(newServiceOrderHistory);

        /// Guarda los registros de las imágenes en la base de datos
        /// Cuando se crean los registros en la BD carga las imágenes al servidor
        imageWithDataList.forEach(img -> serviceOrderImageService.add(serviceOrder, img, author, newServiceOrderHistory));

        /// Registra el movimiento en la bitácora
        auditLogService.create(request, EAuditAction.INSERT,"ServiceOrder",newServiceOrderHistory.getId(),null,
                new tools.jackson.databind.ObjectMapper().writeValueAsString(newServiceOrderHistory));

        /// Retornar id con el nuevo registro creado
        return newServiceOrderHistory.getId();
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request) {
        /// Valída que el registro exista
        ServiceOrderHistoryEntity sohToDelete = serviceOrderHistoryRepository.findById(id).
                orElseThrow(()-> new MyIntegrityValidation("Error: El registro indicado no existe",400));

        /// Elimina las imágenes de la BD y del servidor
        serviceOrderImageService.deleteHistoryImages(sohToDelete);

        auditLogService.create(request, EAuditAction.DELETE, "ServiceOrderHistory", sohToDelete.getId(),
                new tools.jackson.databind.ObjectMapper().writeValueAsString(sohToDelete),null);

        /// Elimina el registro de la Bd
        serviceOrderHistoryRepository.delete(sohToDelete);
    }

    public FullServiceOrderHistoryDTO get(Long id, HttpServletRequest request) {
        UserEntity author = jwtUtils.getUserFromRequest(request);

        ServiceOrderHistoryEntity sohData = serviceOrderHistoryRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("Error: El registro indicado no existe",400));

        return new FullServiceOrderHistoryDTO(sohData, author);
    }

    public void update(@Valid UpdateServiceOrderHistoryDTO serviceOrderHistory, String imageMetadata,
                       HttpServletRequest request) throws JsonProcessingException {
        /// Obtenemos al autor del movimiento
        UserEntity author = jwtUtils.getUserFromRequest(request);

        /// Obtenemos el registro que será actualizado
        ServiceOrderHistoryEntity sohToUpdate = serviceOrderHistoryRepository.findById(serviceOrderHistory.id())
                .orElseThrow(()-> new MyIntegrityValidation("Error: El registro indicado no existe", 400));

        ServiceOrderHistoryEntity oldSOHToUpdate = sohToUpdate;

        /// Debo agregar las validaciones sobre la información recibida:
        validations.forEach(v-> v.validate(serviceOrderHistory.status(), author, sohToUpdate.getServiceOrder()));

        /// Convertimos el JSON de metadata en un objeto manipulable
        List<UpdateServiceOrderImageMetaDataDTO> imageMetadataList = imageService.readJsonData(imageMetadata, UpdateServiceOrderImageMetaDataDTO.class);

        /// Valída la información y crear lista con las imágenes del registro
        List<ImageWithMetadataDTO> imageWithDataList = imageService.validateAndCreateImageWithDataList(serviceOrderHistory.images(), imageMetadataList);

        /// Actualiza las imágenes, primero el registro en la base de datos seguido del archivo en el servidor
        List<Path> pathsToDeleteInDisk = new  ArrayList<>();

        for(var imageWithMetadata:imageWithDataList){
            switch (imageWithMetadata.alter()){
                case ADD:
                    serviceOrderImageService.add(sohToUpdate, imageWithMetadata, author);
                    break;
                case REMOVE:
                    Path deletePath = serviceOrderImageService.remove(sohToUpdate.getServiceOrder(), imageWithMetadata.id());
                    pathsToDeleteInDisk.add(deletePath);
            }
        }
        if(!pathsToDeleteInDisk.isEmpty())
            pathsToDeleteInDisk.forEach(path-> imageService.remove(path));

        /// Por último debo actualizar el registro de la ServiceOrder en la base de datos
        if(serviceOrderHistory.status() != null){
            sohToUpdate.setStatus(serviceOrderHistory.status());
            sohToUpdate.getServiceOrder().setStatus(serviceOrderHistory.status());
        }

        if(serviceOrderHistory.notes() != null)
            sohToUpdate.setNotes(serviceOrderHistory.notes());

        serviceOrderHistoryRepository.save(sohToUpdate);

        /// Agregamos el movimiento a la bitácora
        auditLogService.create(request, EAuditAction.SELECT, "ServiceOrderHistory", sohToUpdate.getId() ,
                new tools.jackson.databind.ObjectMapper().writeValueAsString(oldSOHToUpdate),
                new tools.jackson.databind.ObjectMapper().writeValueAsString(sohToUpdate));

    }

    public List<FullServiceOrderHistoryDTO> getAll(Long id, HttpServletRequest request) {
        ServiceOrderEntity serviceOrderEntity = serviceOrderRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("Error: El registro indicado no existe",400));

        List<FullServiceOrderHistoryDTO> sohList =  serviceOrderHistoryRepository.findByServiceOrderId(id)
                .stream().map(FullServiceOrderHistoryDTO::new).toList();

        auditLogService.create(request, EAuditAction.SELECT, "ServiceOrderHistory", serviceOrderEntity.getId(),
                null, new tools.jackson.databind.ObjectMapper().writeValueAsString(sohList));

        return sohList;
    }
}