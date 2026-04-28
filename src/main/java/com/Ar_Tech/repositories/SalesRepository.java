package com.Ar_Tech.repositories;

import com.Ar_Tech.models.Sales;
import com.Ar_Tech.models.enums.SaleStatus;
import com.Ar_Tech.models.enums.SaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    Optional<Sales> findBySaleFolio(String saleFolio);
    
    Page<Sales> findByClientId(Long clientId, Pageable pageable);
    
    Page<Sales> findByStatus(SaleStatus status, Pageable pageable);
    
    Page<Sales> findBySaleType(SaleType saleType, Pageable pageable);
    
    Page<Sales> findByClientIdAndStatus(Long clientId, SaleStatus status, Pageable pageable);
    
    Page<Sales> findByTotalGreaterThanEqualOrderByCreatedAtDesc(java.math.BigDecimal minTotal, Pageable pageable);
}