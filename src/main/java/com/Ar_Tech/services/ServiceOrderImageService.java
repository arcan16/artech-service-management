package com.Ar_Tech.services;

import com.Ar_Tech.dto.serviceOrderImage.ImageWithMetadataDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.ServiceOrderImageEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.repositories.ServiceOrderImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ServiceOrderImageService {

    @Autowired
    private ImageService imageService;

    @Value("${spring.orders.images}")
    private String orderImagePath;

    @Autowired
    private ServiceOrderImageRepository serviceOrderImageRepository;

    @Transactional
    public void deleteAll(ServiceOrderEntity serviceOrder){
        try {
            imageService.deleteAllImagesOnDirectory(orderImagePath + "/" + serviceOrder.getId());
            serviceOrderImageRepository.deleteAll(serviceOrder.getImages());
        }catch (Exception e) {
            throw new MyIntegrityValidation("Error al eliminar registro",400);
        }
    }

    @Transactional
    public void add(ServiceOrderEntity serviceOrder, ImageWithMetadataDTO imageWithMetadata, UserEntity author){

        /// Genera el path de la imagen
        Path imagePath = getNewImagePath(serviceOrder.getId(), imageWithMetadata.name());

        /// Crea el registro en la base de datos para la imagen
        try {
            ServiceOrderImageEntity newSOImage =  new ServiceOrderImageEntity(serviceOrder, imageWithMetadata,
                    author,imagePath);
            serviceOrderImageRepository.save(newSOImage);
        }catch (Exception e){
            throw new MyIntegrityValidation(e.getMessage(), 400);
        }

        /// Copia la imágen en el directorio del servidor
        imageService.uploadImageToServer(imageWithMetadata.image(), imagePath);
    }

    @Transactional
    public Path remove(ServiceOrderEntity serviceOrder, ImageWithMetadataDTO imageWithMetadata, UserEntity author){
        Path imagePath = null;
        try {
            System.out.println("imagen a buscar " + imageWithMetadata.id());
            ServiceOrderImageEntity serviceOrderImageToRemove = serviceOrderImageRepository.findById(imageWithMetadata.id())
                    .orElseThrow(()-> new MyIntegrityValidation("Error: La imagen indicada no existe",400));

            imagePath = Paths.get(serviceOrderImageToRemove.getImagePath());

            serviceOrderImageRepository.delete(serviceOrderImageToRemove);
        }catch (Exception e){
            throw new MyIntegrityValidation(e.getMessage(),400);
        }
        return imagePath;
        //imageService.remove(imagePath);
    }

    /// Genera el path para una imagen
    public Path getNewImagePath(Long serviceOrderId, String imageName){
        /*String extension = imageName.substring(imageName.lastIndexOf("."));
        String nuevoNombre = UUID.randomUUID().toString() + extension;

        // De esta forma une los segmentos de forma segura usando las diagonales correctas
        return Paths.get(orderImagePath, serviceOrderId.toString(), nuevoNombre);*/

        return Paths.get(orderImagePath + serviceOrderId + "/" + UUID.randomUUID()
                + "." + imageName.substring(imageName.lastIndexOf(".") +1 ));
    }
}
