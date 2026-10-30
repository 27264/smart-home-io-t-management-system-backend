package com.smarthome.iotmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.model.ScheduledAction;

public interface ScheduledActionRepository extends JpaRepository<ScheduledAction, UUID> {

    // B2 - FIND BY STATUS
    List<ScheduledAction> findByStatusOrderByActionDateAsc(ActionStatus status);

    // B3 - FIND BETWEEN DATES
    List<ScheduledAction> findByActionDateBetweenOrderByActionDateAsc(LocalDate start, LocalDate end);
}
