package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnItemRepository extends JpaRepository<ReturnItem, Long> {

    // Derived queries for return items by return, part, quantity checks
}