package com.smarthome.iotmanagement.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smarthome.iotmanagement.model.Protocol;

public interface ProtocolRepository extends JpaRepository<Protocol, UUID> {
}
