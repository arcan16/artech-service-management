package com.Ar_Tech.services;

import com.Ar_Tech.dto.serviceOrderImage.CreateServiceOrderImageMetaDataDTO;
import com.Ar_Tech.dto.serviceOrderImage.CreationServiceOrderImageDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EImageType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.UUID;
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
}
