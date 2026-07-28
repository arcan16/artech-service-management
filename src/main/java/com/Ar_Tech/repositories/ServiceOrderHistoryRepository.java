package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrderHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderHistoryRepository extends JpaRepository<ServiceOrderHistoryEntity, Long> {
    
    Page<ServiceOrderHistoryEntity> findByChangedBy(Long changedBy, Pageable pageable);

    List<ServiceOrderHistoryEntity> findByServiceOrderId(Long id);
}