package com.smarthome.iotmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.smarthome.iotmanagement.model.ScheduledAction;
import com.smarthome.iotmanagement.model.SmartDevice;
import com.smarthome.iotmanagement.model.Controller;
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

    public ScheduledAction saveScheduledAction(ScheduledAction scheduledAction) {
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

    public List<ScheduledAction> getAllScheduledActions() {
        return scheduledActionRepository.findAll();
    }

    public Optional<ScheduledAction> getScheduledActionById(UUID id) {
        return scheduledActionRepository.findById(id);
    }

    public ScheduledAction updateScheduledAction(UUID id, ScheduledAction scheduledActionDetails) {
        Optional<ScheduledAction> existing = scheduledActionRepository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        ScheduledAction scheduledAction = existing.get();
        scheduledAction.setActionDate(scheduledActionDetails.getActionDate());
        scheduledAction.setStatus(scheduledActionDetails.getStatus());
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
}
