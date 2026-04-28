package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrder;
import com.Ar_Tech.models.enums.ServiceOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    Optional<ServiceOrder> findByFolio(String folio);
    
    Page<ServiceOrder> findByStatus(ServiceOrderStatus status, Pageable pageable);
    
    Page<ServiceOrder> findByAssignedToId(Long assignedToId, Pageable pageable);
    
    Page<ServiceOrder> findByCustomerDeviceClientId(Long clientId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"customerDevice", "assignedTo", "createdBy"})
    Page<ServiceOrder> findServiceOrderByStatus(ServiceOrderStatus status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"customerDevice.device", "serviceOrderParts", "serviceOrderHistory"})
    Optional<ServiceOrder> findById(Long id);
    
    Page<ServiceOrder> findByCreatedByIdAndStatus(Long userId, ServiceOrderStatus status, Pageable pageable);
}