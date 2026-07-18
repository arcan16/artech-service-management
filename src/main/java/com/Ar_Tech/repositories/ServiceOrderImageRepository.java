
package com.Ar_Tech.repositories;

import com.Ar_Tech.dto.serviceOrderImage.FullServiceOrderImageDTO;
import com.Ar_Tech.models.ServiceOrderImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceOrderImageRepository extends JpaRepository<ServiceOrderImageEntity, Long> {
    List<ServiceOrderImageEntity> getByServiceOrderId(Long id);
}
