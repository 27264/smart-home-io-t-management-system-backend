package com.smarthome.iotmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.model.ScheduledAction;

public interface ScheduledActionRepository
        extends JpaRepository<ScheduledAction, UUID> {

    // B2 - FIND BY STATUS
    List<ScheduledAction>
            findByStatusOrderByActionDateAsc(
                    ActionStatus status);


    // B3 - FIND BETWEEN DATES
    List<ScheduledAction>
            findByActionDateBetweenOrderByActionDateAsc(
                    LocalDate start,
                    LocalDate end);


    // D1 - CHECK IF CONTROLLER ALREADY HAS
    // A NON-ABORTED ACTION ON SAME DATE
    boolean existsByControllerIdAndActionDateAndStatusNot(
            UUID controllerId,
            LocalDate actionDate,
            ActionStatus status);


    // F1 - COUNT SCHEDULED ACTIONS BY STATUS
    @Query("""
            SELECT sa.status, COUNT(sa)
            FROM ScheduledAction sa
            GROUP BY sa.status
            """)
    List<Object[]> countScheduledActionsByStatus();


    // F2 - CANCEL ALL NON-EXECUTED ACTIONS
    // FOR ONE CONTROLLER ON ONE DATE
    @Modifying
    @Query("""
            UPDATE ScheduledAction sa
            SET sa.status = :abortedStatus
            WHERE sa.controller.id = :controllerId
            AND sa.actionDate = :date
            AND sa.status <> :executedStatus
            """)
    int cancelActionsForControllerOnDate(
            @Param("controllerId") UUID controllerId,
            @Param("date") LocalDate date,
            @Param("abortedStatus") ActionStatus abortedStatus,
            @Param("executedStatus") ActionStatus executedStatus);

    // BONUS - COUNT BY ACTION DATE (TODAY)
    long countByActionDate(LocalDate actionDate);

    // BONUS - COUNT BY STATUS (PLANNED)
    long countByStatus(ActionStatus status);
}
