package com.smarthome.iotmanagement.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smarthome.iotmanagement.model.SmartDevice;

public interface SmartDeviceRepository extends JpaRepository<SmartDevice, UUID> {

    // B1 - DERIVED
    List<SmartDevice> findByBrandIgnoreCaseOrderByDeviceNameAsc(String brand);
}
