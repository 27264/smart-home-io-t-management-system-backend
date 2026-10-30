package com.smarthome.iotmanagement.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.smarthome.iotmanagement.model.Protocol;

public interface ProtocolRepository extends JpaRepository<Protocol, UUID> {

    // C3 - Protocols nobody holds
    @Query("""
            SELECT p
            FROM Protocol p
            WHERE p.controllers IS EMPTY
            """)
    List<Protocol> findUnusedProtocols();
}
