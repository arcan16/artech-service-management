package com.Ar_Tech.repositories;

import com.Ar_Tech.models.DeviceEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    
    Page<DeviceEntity> findByBrandContainingIgnoreCase(String brand, Pageable pageable);
    
    Page<DeviceEntity> findByModelContainingIgnoreCase(String model, Pageable pageable);
    
    Page<DeviceEntity> findByBrandAndModelContainingIgnoreCase(String brand, String model, Pageable pageable);

    boolean existsByBrandAndModel(@NonNull @NotBlank(message = "Brand is required")String brand,
                                  @NonNull @NotBlank(message = "Model is required") String model);

    boolean existsByBrandAndModelAndIdNot(@NonNull @NotBlank(message = "Brand is required") String brand,
                                          @NonNull @NotBlank(message = "Model is required") String model,
                                          @NotNull Long id);
}