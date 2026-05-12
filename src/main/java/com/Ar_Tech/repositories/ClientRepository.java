package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByCustomerCode(String customerCode);
    
    Page<ClientEntity> findByCreditLimitGreaterThanEqual(BigDecimal minCredit, Pageable pageable);
    
    Page<ClientEntity> findByPersonEmailContainingIgnoreCase(String email, Pageable pageable);
    
    Optional<ClientEntity> findByPersonPhone(String phone);
    
    Page<ClientEntity> findByDeletedAtIsNull(Pageable pageable);
}