package com.Ar_Tech.repositories;

import com.Ar_Tech.models.PartsProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartsProductsRepository extends JpaRepository<PartsProducts, Long> {

    Optional<PartsProducts> findBySku(String sku);
    
    Page<PartsProducts> findByNameContainingIgnoreCase(String partName, Pageable pageable);


}