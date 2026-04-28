package com.Ar_Tech.repositories;

import com.Ar_Tech.models.InventoryMovement;
import com.Ar_Tech.models.enums.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    Page<InventoryMovement> findByMovementType(MovementType movementType, Pageable pageable);
    
    Page<InventoryMovement> findByProduct_Id(Long partsProductsId, Pageable pageable);
    
    Page<InventoryMovement> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    Page<InventoryMovement> findByCreatedAtBetweenOrderByCreatedAtDesc(java.time.LocalDateTime start, java.time.LocalDateTime end, Pageable pageable);
}