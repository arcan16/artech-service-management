package com.Ar_Tech.models;

import com.Ar_Tech.dto.device.CreateDeviceDTO;
import com.Ar_Tech.dto.device.UpdateDeviceDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public DeviceEntity(@Valid CreateDeviceDTO deviceDTO) {
        this.brand = deviceDTO.brand();
        this.model = deviceDTO.model();
    }

    public void update(@Valid UpdateDeviceDTO deviceDTO) {
        if(deviceDTO.brand() != null)
            this.brand = deviceDTO.brand();
        if(deviceDTO.model() != null)
            this.model = deviceDTO.model();
    }
}