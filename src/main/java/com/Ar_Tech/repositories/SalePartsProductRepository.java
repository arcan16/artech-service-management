package com.Ar_Tech.repositories;

import com.Ar_Tech.models.SalePartsProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalePartsProductRepository extends JpaRepository<SalePartsProduct, Long> {

    // Queries for sale items by sale, part, quantity
}