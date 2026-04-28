package com.Ar_Tech.repositories;

import com.Ar_Tech.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByCustomerCode(String customerCode);
    
    Page<Client> findByCreditLimitGreaterThanEqual(BigDecimal minCredit, Pageable pageable);
    
    Page<Client> findByPersonEmailContainingIgnoreCase(String email, Pageable pageable);
    
    Optional<Client> findByPersonPhone(String phone);
    
    Page<Client> findByDeletedAtIsNull(Pageable pageable);
}