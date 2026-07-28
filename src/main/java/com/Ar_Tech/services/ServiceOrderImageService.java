package com.Ar_Tech.services;

import com.Ar_Tech.dto.serviceOrderImage.ImageWithMetadataDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.ServiceOrderHistoryEntity;
import com.Ar_Tech.models.ServiceOrderImageEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.repositories.ServiceOrderImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ServiceOrderImageService {

    @Autowired
    private ImageService imageService;

    @Value("${spring.orders.images}")
    private String orderImagePath;

    @Autowired
    private ServiceOrderImageRepository serviceOrderImageRepository;

//    @Transactional
    public void deleteAll(ServiceOrderEntity serviceOrder){
        Path sourceDir = Paths.get(orderImagePath, serviceOrder.getId().toString());

        // Si la carpeta no existe en disco, solo borramos de la BDD
        if (!Files.exists(sourceDir)) {
            serviceOrderImageRepository.deleteAll(serviceOrder.getImages());
            return;
        }

        // Ruta temporal para el respaldo (ej. orderImagePath/backup_123_uuid)
        Path backupDir = Paths.get(orderImagePath, "backup_" + serviceOrder.getId() + "_" + UUID.randomUUID());

        try {
            // 1. Renombrar/Mover la carpeta a la ubicación temporal (Operación atómica en la mayoría de Sistemas Operativos)
            Files.move(sourceDir, backupDir, StandardCopyOption.REPLACE_EXISTING);

            // 2. Intentar eliminar los registros en la Base de Datos
            serviceOrderImageRepository.deleteAll(serviceOrder.getImages());

            // Sincronizar cambios en BDD inmediatamente para forzar cualquier posible error de SQL antes de borrar el backup
            serviceOrderImageRepository.flush();

            // 3. Si la BDD no falló, borramos definitivamente el directorio temporal
            imageService.deleteAllImagesOnDirectory(backupDir.toString());

        } catch (Exception e) {
            // 4. RESTAURACIÓN: Si algo falla en la BDD, regresamos la carpeta a su lugar original
            if (Files.exists(backupDir)) {
                try {
                    Files.move(backupDir, sourceDir, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioException) {
                    // Loggear el error de restauración si ocurriera
                    System.err.println("Error crítico: No se pudo restaurar el directorio de imágenes: " + ioException.getMessage());
                }
            }

            throw new MyIntegrityValidation("Error al eliminar los registros de la orden de servicio", 400);
        }
    }

    public void deleteHistoryImages(ServiceOrderHistoryEntity serviceOrderHistory){
        serviceOrderHistory.getImages().forEach(img-> {
            Path imagePath = remove(serviceOrderHistory.getServiceOrder(), img.getId());
            imageService.remove(imagePath);
        });
    }

    @Transactional
    public void add(ServiceOrderHistoryEntity serviceOrderHistory, ImageWithMetadataDTO imageWithMetadata, UserEntity author){
        this.add(serviceOrderHistory.getServiceOrder(), imageWithMetadata, author, serviceOrderHistory);
    }

    @Transactional
    public void add(ServiceOrderEntity serviceOrder, ImageWithMetadataDTO imageWithMetadata, UserEntity author){
        this.add(serviceOrder, imageWithMetadata, author, null);
    }

    @Transactional
    public void add(ServiceOrderEntity serviceOrder, ImageWithMetadataDTO imageWithMetadata, UserEntity author,
                    ServiceOrderHistoryEntity serviceOrderHistory){

        /// Genera el path de la imagen
        Path imagePath = getNewImagePath(serviceOrder.getId(), imageWithMetadata.name());

        /// Crea el registro en la base de datos para la imagen
        try {
            ServiceOrderImageEntity newSOImage =  new ServiceOrderImageEntity(serviceOrder, imageWithMetadata,
                    author,imagePath, serviceOrderHistory);
            serviceOrderImageRepository.save(newSOImage);
        }catch (Exception e){
            throw new MyIntegrityValidation(e.getMessage(), 400);
        }

        /// Copia la imágen en el directorio del servidor
        imageService.uploadImageToServer(imageWithMetadata.image(), imagePath);
    }

    @Transactional
    public Path remove(ServiceOrderEntity serviceOrder, Long imageWithMetadata){
        Path imagePath = null;
        try {
            ServiceOrderImageEntity serviceOrderImageToRemove = serviceOrderImageRepository.findById(imageWithMetadata)
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
