package com.smarthome.iotmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.model.Controller;
import com.smarthome.iotmanagement.model.ScheduledAction;
import com.smarthome.iotmanagement.model.SmartDevice;
import com.smarthome.iotmanagement.repository.ControllerRepository;
import com.smarthome.iotmanagement.repository.ScheduledActionRepository;
import com.smarthome.iotmanagement.repository.SmartDeviceRepository;

@Service
public class ScheduledActionService {

    private final ScheduledActionRepository scheduledActionRepository;
    private final SmartDeviceRepository smartDeviceRepository;
    private final ControllerRepository controllerRepository;

    public ScheduledActionService(
            ScheduledActionRepository scheduledActionRepository,
            SmartDeviceRepository smartDeviceRepository,
            ControllerRepository controllerRepository) {
        this.scheduledActionRepository = scheduledActionRepository;
        this.smartDeviceRepository = smartDeviceRepository;
        this.controllerRepository = controllerRepository;
    }

    // D1 - SAVE SCHEDULED ACTION
    public ScheduledAction saveScheduledAction(ScheduledAction scheduledAction) {
        scheduledAction.setStatus(ActionStatus.PLANNED);
        if (scheduledAction.getSmartDevice() != null && scheduledAction.getSmartDevice().getId() != null) {
            Optional<SmartDevice> smartDevice = smartDeviceRepository.findById(scheduledAction.getSmartDevice().getId());
            smartDevice.ifPresent(scheduledAction::setSmartDevice);
        }
        if (scheduledAction.getController() != null && scheduledAction.getController().getId() != null) {
            Optional<Controller> controller = controllerRepository.findById(scheduledAction.getController().getId());
            controller.ifPresent(scheduledAction::setController);
        }
        return scheduledActionRepository.save(scheduledAction);
    }

    // D1 - CHECK DUPLICATE CONTROLLER + DATE
    public boolean controllerAlreadyHasAction(UUID controllerId, LocalDate actionDate) {
        return scheduledActionRepository.existsByControllerIdAndActionDateAndStatusNot(
                controllerId, actionDate, ActionStatus.ABORTED);
    }

    public List<ScheduledAction> getAllScheduledActions() {
        return scheduledActionRepository.findAll();
    }

    public Optional<ScheduledAction> getScheduledActionById(UUID id) {
        return scheduledActionRepository.findById(id);
    }

    // GENERAL UPDATE - STATUS MUST NOT CHANGE HERE
    public ScheduledAction updateScheduledAction(UUID id, ScheduledAction scheduledActionDetails) {
        Optional<ScheduledAction> existingScheduledAction = scheduledActionRepository.findById(id);
        if (existingScheduledAction.isEmpty()) {
            return null;
        }
        ScheduledAction scheduledAction = existingScheduledAction.get();
        scheduledAction.setActionDate(scheduledActionDetails.getActionDate());
        scheduledAction.setCommand(scheduledActionDetails.getCommand());
        if (scheduledActionDetails.getSmartDevice() != null && scheduledActionDetails.getSmartDevice().getId() != null) {
            Optional<SmartDevice> smartDevice = smartDeviceRepository.findById(scheduledActionDetails.getSmartDevice().getId());
            smartDevice.ifPresent(scheduledAction::setSmartDevice);
        }
        if (scheduledActionDetails.getController() != null && scheduledActionDetails.getController().getId() != null) {
            Optional<Controller> controller = controllerRepository.findById(scheduledActionDetails.getController().getId());
            controller.ifPresent(scheduledAction::setController);
        }
        return scheduledActionRepository.save(scheduledAction);
    }

    public String deleteScheduledAction(UUID id) {
        if (!scheduledActionRepository.existsById(id)) {
            return "The scheduled action with that id does not exist";
        }
        scheduledActionRepository.deleteById(id);
        return "Scheduled action deleted successfully";
    }

    public List<ScheduledAction> getScheduledActionsByStatus(ActionStatus status) {
        return scheduledActionRepository.findByStatusOrderByActionDateAsc(status);
    }

    public List<ScheduledAction> getScheduledActionsBetweenDates(LocalDate start, LocalDate end) {
        return scheduledActionRepository.findByActionDateBetweenOrderByActionDateAsc(start, end);
    }

    // D2 - CHANGE STATUS
    public String changeStatus(UUID id, ActionStatus newStatus) {
        Optional<ScheduledAction> optionalAction = scheduledActionRepository.findById(id);
        if (optionalAction.isEmpty()) {
            return "NOT_FOUND";
        }
        ScheduledAction action = optionalAction.get();
        ActionStatus currentStatus = action.getStatus();
        boolean allowed = false;

        if (currentStatus == ActionStatus.PLANNED) {
            if (newStatus == ActionStatus.QUEUED || newStatus == ActionStatus.ABORTED) {
                allowed = true;
            }
        } else if (currentStatus == ActionStatus.QUEUED) {
            if (newStatus == ActionStatus.EXECUTED || newStatus == ActionStatus.ABORTED) {
                allowed = true;
            }
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        action.setStatus(newStatus);
        scheduledActionRepository.save(action);
        return "SUCCESS";
    }
}
