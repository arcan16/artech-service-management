package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderHistoryRepository extends JpaRepository<ServiceOrderHistory, Long> {
    
    Page<ServiceOrderHistory> findByChangedBy(Long changedBy, Pageable pageable);
}