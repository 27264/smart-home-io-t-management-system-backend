package com.smarthome.iotmanagement.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smarthome.iotmanagement.model.ScheduledAction;

public interface ScheduledActionRepository extends JpaRepository<ScheduledAction, UUID> {
}
