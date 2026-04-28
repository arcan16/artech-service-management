package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrderParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderPartsRepository extends JpaRepository<ServiceOrderParts, Long> {

    // Queries for parts in service orders by serviceOrder, partsProducts
}