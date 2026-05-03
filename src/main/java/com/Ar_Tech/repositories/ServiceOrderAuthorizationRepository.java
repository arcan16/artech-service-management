package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrderAuthorization;
import com.Ar_Tech.models.enums.EAuthorizationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderAuthorizationRepository extends JpaRepository<ServiceOrderAuthorization, Long> {

    Page<ServiceOrderAuthorization> findByServiceOrderId(Long serviceOrderId, Pageable pageable);
    
    Page<ServiceOrderAuthorization> findByAuthorizationStatus(EAuthorizationStatus status, Pageable pageable);
}