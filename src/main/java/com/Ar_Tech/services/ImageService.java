package com.Ar_Tech.services;

import com.Ar_Tech.dto.serviceOrderImage.CreateServiceOrderImageMetaDataDTO;
import com.Ar_Tech.dto.serviceOrderImage.CreationServiceOrderImageDTO;
import com.Ar_Tech.dto.serviceOrderImage.IImageMetadata;
import com.Ar_Tech.dto.serviceOrderImage.ImageWithMetadataDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EImageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageService {

    public Path getDirectoryPath(String imagePath, Long serviceOrderId){
        return Paths.get(imagePath + "/" + serviceOrderId);
    }

    public CreationServiceOrderImageDTO saveImageOnServer(CreateServiceOrderImageMetaDataDTO soi, MultipartFile img,
                                                          String imagePath, ServiceOrderEntity serviceOrder, EImageType imageType,
                                                          UserEntity author) {
        // Creamos la ruta del directorio
        Path directoryPath = getDirectoryPath(imagePath, serviceOrder.getId());

        Path imgPath = Paths.get(imagePath + serviceOrder.getId() + "/" + UUID.randomUUID()
                + "." + soi.name().substring(soi.name().lastIndexOf(".") +1 ));

        /// Primero verificamos si el directorio existe:
        /// - Si existe el directorio cargamos las imágenes al servidor
        /// - Si no existe el directorio, lo creamos y después cargamos las imágenes al servidor
        if(Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            uploadImageToServer(img, imgPath);
        }else{
            try {
                Files.createDirectories(directoryPath);
                uploadImageToServer(img, imgPath);
            }catch (IOException e) {
                throw new MyIntegrityValidation(e.getMessage(), 400);
            }
        }
        return new CreationServiceOrderImageDTO(serviceOrder, imgPath.toString(), imageType, soi, author);
    }

    public void deleteAllImagesOnDirectory(String directory){

        Path root = Paths.get(directory);

        if (!Files.exists(root))
            return;

        try (Stream<Path> paths = Files.walk(root)) {

            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadImageToServer(MultipartFile img, Path path) {
        if(!Files.exists(path.getParent()) || Files.isDirectory(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            }catch (IOException e) {
                throw new MyIntegrityValidation(e.getMessage(), 400);
            }
        }
        try {
            Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            remove(path);
            throw new MyIntegrityValidation(e.toString(),400);
        }
    }
    public void remove(Path imagePath){
        try{
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /// Deserializa variables JSON y retorna un objeto con la información convertida en un objeto
    public <T> List<T> readJsonData(String jsonData, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

        return mapper.readValue(jsonData, type);
    }
}
