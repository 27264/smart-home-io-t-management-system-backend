package com.smarthome.iotmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.model.Controller;
import com.smarthome.iotmanagement.model.ScheduledAction;
import com.smarthome.iotmanagement.model.SmartDevice;
import com.smarthome.iotmanagement.repository.ControllerRepository;
import com.smarthome.iotmanagement.repository.ScheduledActionRepository;
import com.smarthome.iotmanagement.repository.SmartDeviceRepository;
import com.smarthome.iotmanagement.repository.StatusReadingRepository;

@Service
public class ScheduledActionService {

    private final ScheduledActionRepository scheduledActionRepository;
    private final SmartDeviceRepository smartDeviceRepository;
    private final ControllerRepository controllerRepository;
    private final StatusReadingRepository statusReadingRepository;


    public ScheduledActionService(
            ScheduledActionRepository scheduledActionRepository,
            SmartDeviceRepository smartDeviceRepository,
            ControllerRepository controllerRepository,
            StatusReadingRepository statusReadingRepository) {

        this.scheduledActionRepository =
                scheduledActionRepository;

        this.smartDeviceRepository =
                smartDeviceRepository;

        this.controllerRepository =
                controllerRepository;

        this.statusReadingRepository =
                statusReadingRepository;
    }


    // D1 - SAVE SCHEDULED ACTION
    public ScheduledAction saveScheduledAction(
            ScheduledAction scheduledAction) {

        // Every new scheduled action starts as PLANNED
        scheduledAction.setStatus(
                ActionStatus.PLANNED);

        // Get real SmartDevice from database
        if (scheduledAction.getSmartDevice() != null
                && scheduledAction
                        .getSmartDevice()
                        .getId() != null) {

            Optional<SmartDevice> smartDevice =
                    smartDeviceRepository.findById(
                            scheduledAction
                                     .getSmartDevice()
                                    .getId());

            if (smartDevice.isPresent()) {

                scheduledAction.setSmartDevice(
                        smartDevice.get());
            }
        }


        // Get real Controller from database
        if (scheduledAction.getController() != null
                && scheduledAction
                        .getController()
                        .getId() != null) {

            Optional<Controller> controller =
                    controllerRepository.findById(
                            scheduledAction
                                    .getController()
                                    .getId());

            if (controller.isPresent()) {

                scheduledAction.setController(
                        controller.get());
            }
        }


        return scheduledActionRepository.save(
                scheduledAction);
    }


    // D1 - CHECK DUPLICATE CONTROLLER + DATE
    public boolean controllerAlreadyHasAction(
            UUID controllerId,
            LocalDate actionDate) {

        return scheduledActionRepository
                .existsByControllerIdAndActionDateAndStatusNot(
                        controllerId,
                        actionDate,
                        ActionStatus.ABORTED);
    }


    // READ ALL
    public List<ScheduledAction>
            getAllScheduledActions() {

        return scheduledActionRepository
                .findAll();
    }


    // READ BY ID
    public Optional<ScheduledAction>
            getScheduledActionById(
                    UUID id) {

        return scheduledActionRepository
                .findById(id);
    }


    // GENERAL UPDATE
    // STATUS MUST NOT CHANGE HERE
    public ScheduledAction updateScheduledAction(
            UUID id,
            ScheduledAction scheduledActionDetails) {

        Optional<ScheduledAction>
                existingScheduledAction =
                scheduledActionRepository
                        .findById(id);

        if (existingScheduledAction.isEmpty()) {

            return null;
        }


        ScheduledAction scheduledAction =
                existingScheduledAction.get();


        scheduledAction.setActionDate(
                scheduledActionDetails
                        .getActionDate());

        scheduledAction.setCommand(
                scheduledActionDetails
                        .getCommand());


        // IMPORTANT:
        // DO NOT CHANGE STATUS HERE


        // Update SmartDevice if supplied
        if (scheduledActionDetails.getSmartDevice() != null
                && scheduledActionDetails
                        .getSmartDevice()
                        .getId() != null) {

            Optional<SmartDevice> smartDevice =
                    smartDeviceRepository.findById(
                            scheduledActionDetails
                                    .getSmartDevice()
                                    .getId());

            if (smartDevice.isPresent()) {

                scheduledAction.setSmartDevice(
                        smartDevice.get());
            }
        }


        // Update Controller if supplied
        if (scheduledActionDetails.getController() != null
                && scheduledActionDetails
                        .getController()
                        .getId() != null) {

            Optional<Controller> controller =
                    controllerRepository.findById(
                            scheduledActionDetails
                                    .getController()
                                    .getId());

            if (controller.isPresent()) {

                scheduledAction.setController(
                        controller.get());
            }
        }


        return scheduledActionRepository.save(
                scheduledAction);
    }


    // E3 - DELETE WITH STATUS READING PROTECTION
    public String deleteScheduledAction(
            UUID id) {

        if (!scheduledActionRepository
                .existsById(id)) {

            return "NOT_FOUND";
        }


        if (statusReadingRepository
                .existsByScheduledActionId(id)) {

            return "HAS_READINGS";
        }


        scheduledActionRepository
                .deleteById(id);

        return "SUCCESS";
    }


    // B2 - FIND BY STATUS
    public List<ScheduledAction>
            getScheduledActionsByStatus(
                    ActionStatus status) {

        return scheduledActionRepository
                .findByStatusOrderByActionDateAsc(
                        status);
    }


    // B3 - FIND BETWEEN DATES
    public List<ScheduledAction>
            getScheduledActionsBetweenDates(
                    LocalDate start,
                    LocalDate end) {

        return scheduledActionRepository
                .findByActionDateBetweenOrderByActionDateAsc(
                        start,
                        end);
    }


    // D2 - CHANGE STATUS
    public String changeStatus(
            UUID id,
            ActionStatus newStatus) {

        Optional<ScheduledAction> optionalAction =
                scheduledActionRepository
                        .findById(id);


        if (optionalAction.isEmpty()) {

            return "NOT_FOUND";
        }


        ScheduledAction action =
                optionalAction.get();

        ActionStatus currentStatus =
                action.getStatus();

        boolean allowed = false;


        // PLANNED -> QUEUED or ABORTED
        if (currentStatus
                == ActionStatus.PLANNED) {

            if (newStatus
                    == ActionStatus.QUEUED
                    || newStatus
                    == ActionStatus.ABORTED) {

                allowed = true;
            }
        }


        // QUEUED -> EXECUTED or ABORTED
        else if (currentStatus
                == ActionStatus.QUEUED) {

            if (newStatus
                    == ActionStatus.EXECUTED
                    || newStatus
                    == ActionStatus.ABORTED) {

                allowed = true;
            }
        }


        // EXECUTED and ABORTED are final states
        if (!allowed) {

            return "Cannot change status from "
                    + currentStatus
                    + " to "
                    + newStatus;
        }


        action.setStatus(
                newStatus);

        scheduledActionRepository
                .save(action);

        return "SUCCESS";
    }


    // F1 - COUNT ACTIONS BY STATUS
    public List<Object[]>
            getScheduledActionStatsByStatus() {

        return scheduledActionRepository
                .countScheduledActionsByStatus();
    }


    // F2 - CANCEL ALL NON-EXECUTED
    // ACTIONS FOR ONE CONTROLLER
    // ON ONE DATE
    @Transactional
    public int cancelControllerActionsForDay(
            UUID controllerId,
            LocalDate date) {

        return scheduledActionRepository
                .cancelActionsForControllerOnDate(
                        controllerId,
                        date,
                        ActionStatus.ABORTED,
                        ActionStatus.EXECUTED);
    }

    // BONUS - PAGINATION
    public Page<ScheduledAction> getScheduledActionsPage(
            Pageable pageable) {
        return scheduledActionRepository.findAll(pageable);
    }
}
