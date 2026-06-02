
package com.Ar_Tech.models;
import com.Ar_Tech.dto.customerDevice.CreateCustomerDeviceDTO;
import com.Ar_Tech.dto.customerDevice.UpdateCustomerDeviceDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private DeviceEntity device;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "imei", length = 50)
    private String imei;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public CustomerDeviceEntity(ClientEntity client, DeviceEntity device, CreateCustomerDeviceDTO customerDeviceDTO) {
        this.client = client;
        this.device = device;

        if(customerDeviceDTO.serialNumber() != null)
            this.serialNumber = customerDeviceDTO.serialNumber();

        if(customerDeviceDTO.imei() != null)
            this.imei = customerDeviceDTO.imei();
    }

    public void update(@Valid UpdateCustomerDeviceDTO customerDeviceDTO, ClientEntity client, DeviceEntity device) {
        this.client = client;

        this.device = device;

        if(customerDeviceDTO.serialNumber() != null)
            this.serialNumber = customerDeviceDTO.serialNumber();

        if(customerDeviceDTO.imei() != null)
            this.imei = customerDeviceDTO.imei();
    }
}