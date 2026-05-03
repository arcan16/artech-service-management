
package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceOrderImageRepository extends JpaRepository<ServiceOrderImage, Long> {
}
