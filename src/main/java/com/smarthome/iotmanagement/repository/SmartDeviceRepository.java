package com.smarthome.iotmanagement.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.smarthome.iotmanagement.model.SmartDevice;

public interface SmartDeviceRepository extends JpaRepository<SmartDevice, UUID> {

    // B1 - DERIVED
    List<SmartDevice> findByBrandIgnoreCaseOrderByDeviceNameAsc(String brand);

    // C4 - JPQL
    @Query("""
            SELECT DISTINCT sa.smartDevice
            FROM ScheduledAction sa
            WHERE sa.controller.id = :controllerId
            """)
    List<SmartDevice> findDevicesOfController(@Param("controllerId") UUID controllerId);
}
