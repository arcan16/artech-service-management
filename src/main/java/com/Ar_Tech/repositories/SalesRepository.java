package com.Ar_Tech.repositories;

import com.Ar_Tech.models.Sales;
import com.Ar_Tech.models.enums.ESaleStatus;
import com.Ar_Tech.models.enums.ESaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    Optional<Sales> findBySaleFolio(String saleFolio);
    
    Page<Sales> findByClientId(Long clientId, Pageable pageable);
    
    Page<Sales> findByStatus(ESaleStatus status, Pageable pageable);
    
    Page<Sales> findBySaleType(ESaleType saleType, Pageable pageable);
    
    Page<Sales> findByClientIdAndStatus(Long clientId, ESaleStatus status, Pageable pageable);
    
    Page<Sales> findByTotalGreaterThanEqualOrderByCreatedAtDesc(java.math.BigDecimal minTotal, Pageable pageable);
}