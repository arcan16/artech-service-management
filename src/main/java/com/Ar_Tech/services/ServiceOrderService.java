package com.Ar_Tech.services;

import com.Ar_Tech.dto.serviceOrderImage.*;
import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import com.Ar_Tech.dto.serviceOrder.FullServiceOrderDTO;
import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.CustomerDeviceEntity;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.ServiceOrderImageEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.models.enums.EImageType;
import com.Ar_Tech.repositories.CustomerDeviceRepository;
import com.Ar_Tech.repositories.ServiceOrderImageRepository;
import com.Ar_Tech.repositories.ServiceOrderRepository;
import com.Ar_Tech.repositories.UserRepository;
import com.Ar_Tech.validations.serviceOrder.create.IServiceOrderValidation;
import com.Ar_Tech.validations.serviceOrder.update.IUpdateServiceOrderValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private List<IServiceOrderValidation> createValidation = new ArrayList<>();

    @Autowired
    private List<IUpdateServiceOrderValidation> updateValidation = new ArrayList<>();

    @Autowired
    private ImageService  imageService;

    @Value("${spring.orders.images}")
    private String orderImagePath;

    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ServiceOrderImageRepository serviceOrderImageRepository;

    @Autowired
    private ServiceOrderImageService serviceOrderImageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public Long create(CreateServiceOrderDTO serviceOrderDTO, String imageMetadata,
                                HttpServletRequest request) throws JsonProcessingException {
        /// Validaciones
        createValidation.forEach(v-> v.validate(serviceOrderDTO, request));

        UserEntity author = jwtUtils.getUserFromRequest(request);

        Long recordId = null;

        ObjectMapper mapper = new ObjectMapper();
        List<CreationServiceOrderImageDTO> serviceOrderImageDTOList = new  ArrayList<>();

        /// Des-serializa los metadatos de las imágenes y lo convertimos el JSON de metadata en un objeto manipulable
        List<CreateServiceOrderImageMetaDataDTO> imagesMetaData = readJsonData(imageMetadata, CreateServiceOrderImageMetaDataDTO.class);

        /// Valída la información y crear lista con las imágenes del registro
        List<ImageWithMetadataDTO> imageWithDataList = validateAndCreateImageWithDataList(serviceOrderDTO.images(), imagesMetaData);

        /// Creamos el folio para el registro
        String folio = generateFolio(serviceOrderDTO.customerDevice());

        /// Obtenemos el customerDevice recibido
        CustomerDeviceEntity customerDevice = customerDeviceRepository.findById(serviceOrderDTO.customerDevice())
                .orElseThrow(()-> new MyIntegrityValidation("Error: El dispositivo indicado no existe",400));

        /// Formamos el objeto para crear un nuevo ServiceOrder y guardamos el registro
        ServiceOrderEntity newServiceOrder = new ServiceOrderEntity(folio, serviceOrderDTO, customerDevice, author);
        serviceOrderRepository.save(newServiceOrder);

        /// Guarda los registros de las imágenes en la base de datos
        /// Cuando se crean los registros en la BD carga las imágenes al servidor
        imageWithDataList.forEach(img-> serviceOrderImageService.add(newServiceOrder, img, author));

        auditLogService.create(request, EAuditAction.INSERT,"ServiceOrder",newServiceOrder.getId(),null,
                new tools.jackson.databind.ObjectMapper().writeValueAsString(newServiceOrder));

        return newServiceOrder.getId();
    }

    // Genera un folio con formato {customer_device.id}+{LocalDate}
    private String generateFolio(Long customerDevice){
        return customerDevice + LocalDate.now().toString();
    }

    public FullServiceOrderDTO getById(Long id) {
        ServiceOrderEntity serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("Error: El registro no existe",400));

        return new FullServiceOrderDTO(serviceOrder);
    }

    public List<FullServiceOrderImageDTO> getImagesOfServiceOrder(Long id, HttpServletRequest request) {
        List<FullServiceOrderImageDTO> imagesList = serviceOrderImageRepository.getByServiceOrderId(id).stream()
                .map(FullServiceOrderImageDTO::new).toList();
        if (imagesList.isEmpty())
            throw new MyIntegrityValidation("La orden de servicio no cuenta con imagenes registradas",400);

        return imagesList;
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request) {
        /// Validaremos que el registro exista
        ServiceOrderEntity serviceOrderToDelete = serviceOrderRepository.findById(id)
                .orElseThrow(()-> new MyIntegrityValidation("Error: El registro indicado no existe",400));

        /// Ahora debo eliminar las imágenes del directorio
        /// Después debo eliminar el registro de serviceOrderImages
        serviceOrderImageService.deleteAll(serviceOrderToDelete);

        auditLogService.create(request, EAuditAction.DELETE,"ServiceOrders",serviceOrderToDelete.getId(),
                new tools.jackson.databind.ObjectMapper().writeValueAsString(serviceOrderToDelete),
                null);

        /// Por último será eliminado el registro de serviceOrder
        serviceOrderRepository.delete(serviceOrderToDelete);
    }

    public void update(@Valid UpdateServiceOrderDTO serviceOrder, String imageMetadata, HttpServletRequest request) throws JsonProcessingException {
        /// Obtenemos al autor del movimiento
        UserEntity author = jwtUtils.getUserFromRequest(request);

        /// Obtenemos el registro que será actualizado
        ServiceOrderEntity soToUpdate = serviceOrderRepository.findById(serviceOrder.id())
                .orElseThrow(()-> new MyIntegrityValidation("Error: El registro indicado no existe",400));

        ServiceOrderEntity oldValues = soToUpdate;

        /// Debo agregar las validaciones sobre la información recibida:
        updateValidation.forEach(v-> v.validation(serviceOrder, soToUpdate, request));

        /// Convertimos el JSON de metadata en un objeto manipulable
        List<UpdateServiceOrderImageMetaDataDTO> imageMetadataList = readJsonData(imageMetadata, UpdateServiceOrderImageMetaDataDTO.class);

        /// Valída la información y crear lista con las imágenes del registro
        List<ImageWithMetadataDTO> imageWithDataList = validateAndCreateImageWithDataList(serviceOrder.images(), imageMetadataList);

        /// Actualiza las imágenes, primero el registro en la base de datos seguido del archivo en el servidor
        List<Path> pathsToDeleteInDisk = new  ArrayList<>();

        for(var imageWithMetadata:imageWithDataList){
            switch (imageWithMetadata.alter()){
                case ADD:
                    serviceOrderImageService.add(soToUpdate, imageWithMetadata, author);
                    break;
                case REMOVE:
                    Path deletePath = serviceOrderImageService.remove(soToUpdate, imageWithMetadata, author);
                    pathsToDeleteInDisk.add(deletePath);
            }
        }
        if(!pathsToDeleteInDisk.isEmpty())
            pathsToDeleteInDisk.forEach(path-> imageService.remove(path));

        /// Por último debo actualizar el registro de la ServiceOrder en la base de datos
        if(serviceOrder.customerDeviceId() != null){
            soToUpdate.setCustomerDevice(customerDeviceRepository.findById(serviceOrder.customerDeviceId())
                    .orElseThrow(()-> new MyIntegrityValidation("Error: el dispositivo indicado no existe",400)));
        }
        if(serviceOrder.assignedTo() != null){
            soToUpdate.setAssignedTo(userRepository.findById(serviceOrder.assignedTo())
                    .orElseThrow(()-> new MyIntegrityValidation("Error: El técnico al que se intenta asignar no existe", 400)));
        }

        soToUpdate.update(serviceOrder);
        serviceOrderRepository.save(soToUpdate);

        auditLogService.create(request, EAuditAction.UPDATE,"ServiceOrder",soToUpdate.getId(),
                new tools.jackson.databind.ObjectMapper().writeValueAsString(oldValues),
                new tools.jackson.databind.ObjectMapper().writeValueAsString(serviceOrder));
    }

    /// Deserializa variables JSON y retorna un objeto con la información convertida en un objeto
    public <T> List<T> readJsonData(String jsonData, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

        return mapper.readValue(jsonData, type);
    }


    /// Realiza la validación de información de los metadatos y las imágenes. Después crea una lista con objetos que contengan
    /// tanto la imagen como su información
    public List<ImageWithMetadataDTO> validateAndCreateImageWithDataList(List<MultipartFile> images,
                                                                         List<? extends IImageMetadata> imagesMetadata){

        List<ImageWithMetadataDTO> dataList = new ArrayList<>();

        // 1. Validar primero que las cantidades coincidan. Si no, lanzar error directo.
        if (images.size() != imagesMetadata.size()) {
            throw new MyIntegrityValidation("Error: La cantidad de imágenes no coincide con los metadatos", 400);
        }

        // 2. Indexar los metadatos en un Map por nombre para búsquedas O(1)
        Map<String, ? extends IImageMetadata> metadataMap = imagesMetadata.stream()
                .collect(Collectors.toMap(
                        IImageMetadata::name,
                        meta -> meta
                ));

        // 3. Validar la coherencia de cada imagen y crear el objeto
        images.forEach(image -> {
            String fileName = image.getOriginalFilename();
            if (!metadataMap.containsKey(fileName)) {
                throw new MyIntegrityValidation("Error: Metadatos incorrectos para la imagen: " + fileName, 400);
            }

            dataList.add(new ImageWithMetadataDTO(image, metadataMap.get(fileName)));
        });

        if(dataList.isEmpty()){
            return null;
        }

        return dataList;
    }

    public List<FullServiceOrderDTO> getAll(Pageable page, HttpServletRequest request) {
        return serviceOrderRepository.findAll(page).map(FullServiceOrderDTO::new).stream().toList();
    }
}