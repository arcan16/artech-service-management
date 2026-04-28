package com.Ar_Tech.repositories;

import com.Ar_Tech.models.Payments;
import com.Ar_Tech.models.enums.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    Page<Payments> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);
    
    Page<Payments> findBySale_Id(Long salesId, Pageable pageable);
    
    Page<Payments> findByAmountGreaterThanEqualOrderByCreatedAtDesc(java.math.BigDecimal minAmount, Pageable pageable);
}