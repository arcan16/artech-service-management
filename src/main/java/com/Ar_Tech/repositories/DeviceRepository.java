package com.Ar_Tech.repositories;

import com.Ar_Tech.models.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    Page<Device> findByBrandContainingIgnoreCase(String brand, Pageable pageable);
    
    Page<Device> findByModelContainingIgnoreCase(String model, Pageable pageable);
    
    Page<Device> findByBrandAndModelContainingIgnoreCase(String brand, String model, Pageable pageable);
}