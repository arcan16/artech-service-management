package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ClientCredits;
import com.Ar_Tech.models.enums.ECreditStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCreditsRepository extends JpaRepository<ClientCredits, Long> {

    Page<ClientCredits> findByClientId(Long clientId, Pageable pageable);
    
    Page<ClientCredits> findByStatus(ECreditStatus status, Pageable pageable);
    
    Page<ClientCredits> findByStatusAndTotalAmountGreaterThan(ECreditStatus status, java.math.BigDecimal minAmount, Pageable pageable);
}