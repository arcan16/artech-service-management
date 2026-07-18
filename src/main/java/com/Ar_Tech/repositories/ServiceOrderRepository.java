package com.Ar_Tech.repositories;

import com.Ar_Tech.models.CustomerDeviceEntity;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrderEntity, Long> {

    Optional<ServiceOrderEntity> findByFolio(String folio);
    
    Page<ServiceOrderEntity> findByStatus(EServiceOrderStatus status, Pageable pageable);
    
    Page<ServiceOrderEntity> findByAssignedToId(Long assignedToId, Pageable pageable);
    
    Page<ServiceOrderEntity> findByCustomerDeviceClientId(Long clientId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"customerDevice", "assignedTo", "createdBy"})
    Page<ServiceOrderEntity> findServiceOrderByStatus(EServiceOrderStatus status, Pageable pageable);
    
//    @EntityGraph(attributePaths = {"customerDevice.device", "serviceOrderParts", "serviceOrderHistory"})
//    Optional<ServiceOrderEntity> findById(Long id);
    
    Page<ServiceOrderEntity> findByCreatedByIdAndStatus(Long userId, EServiceOrderStatus status, Pageable pageable);

    Optional<ServiceOrderEntity> findByCustomerDeviceId(Long customerDeviceId);

    Optional<ServiceOrderEntity> findByCustomerDeviceAndDeliveredAtIsNull(CustomerDeviceEntity customerDevice);

    boolean existsByCustomerDeviceIdAndIdNot(Long customerDeviceId, @NotNull Long serviceOrderId);

    boolean existsByCustomerDeviceIdAndIdNotAndStatusNot(Long customerDeviceId, @NotNull Long serviceOrderId,
                                                         EServiceOrderStatus status);
}

/*
    @Query("""
            SELECT EXISTS(
                SELECT 1 FROM ServiceOrderEntity s
                WHERE s.customerDevice.id = :customerDeviceId
                AND s.deliveredAt NOT
            )
            """)
    Optional<ServiceOrderEntity> findByCustomerDevice(Long customerDeviceId);
/*/