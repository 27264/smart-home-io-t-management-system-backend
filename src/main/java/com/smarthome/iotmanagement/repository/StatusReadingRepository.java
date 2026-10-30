package com.smarthome.iotmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smarthome.iotmanagement.model.StatusReading;

public interface StatusReadingRepository
        extends JpaRepository<StatusReading, UUID> {

    // E2 - JPQL
    @Query("""
            SELECT sr
            FROM StatusReading sr
            WHERE sr.scheduledAction.smartDevice.serialNumber = :serialNumber
            ORDER BY sr.readDate DESC
            """)
    List<StatusReading> findBySmartDeviceSerialNumber(
            @Param("serialNumber") String serialNumber);

    // E3 - Derived Query
    boolean existsByScheduledActionId(
            UUID scheduledActionId);
}