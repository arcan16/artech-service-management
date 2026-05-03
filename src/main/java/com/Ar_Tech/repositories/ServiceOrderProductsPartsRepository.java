
package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrderProductsParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderProductsPartsRepository extends JpaRepository<ServiceOrderProductsParts, Long> {
}
