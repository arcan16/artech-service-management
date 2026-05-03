package com.Ar_Tech.repositories;

import com.Ar_Tech.models.Returns;
import com.Ar_Tech.models.enums.EReturnStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnsRepository extends JpaRepository<Returns, Long> {

    Page<Returns> findByStatus(EReturnStatus status, Pageable pageable);
    
    Page<Returns> findBySale_Id(Long salesId, Pageable pageable);
    
    Page<Returns> findByClientId(Long clientId, Pageable pageable);
}