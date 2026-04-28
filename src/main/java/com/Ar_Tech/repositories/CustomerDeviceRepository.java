package com.Ar_Tech.repositories;

import com.Ar_Tech.models.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDeviceRepository extends JpaRepository<CustomerDevice, Long> {
}
