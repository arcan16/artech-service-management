package com.Ar_Tech.repositories;

import com.Ar_Tech.models.CustomerDeviceEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDeviceRepository extends JpaRepository<CustomerDeviceEntity, Long> {
    boolean existsByImei(String imei);

    boolean existsBySerialNumber(String serialNumber);

    boolean existsBySerialNumberAndImei(String serialNumber, String imei);

    boolean existsBySerialNumberAndImeiAndIdNot(String serialNumber, String imei, @NotNull Long id);

    boolean existsByImeiAndIdNot(String imei, @NotNull Long id);

    boolean existsBySerialNumberAndIdNot(String serialNumber, @NotNull Long id);
}
