package com.smarthome.iotmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.model.ScheduledAction;
import com.smarthome.iotmanagement.model.StatusReading;
import com.smarthome.iotmanagement.repository.ScheduledActionRepository;
import com.smarthome.iotmanagement.repository.StatusReadingRepository;

@Service
public class StatusReadingService {

    private final StatusReadingRepository
            statusReadingRepository;

    private final ScheduledActionRepository
            scheduledActionRepository;

    public StatusReadingService(
            StatusReadingRepository statusReadingRepository,
            ScheduledActionRepository scheduledActionRepository) {

        this.statusReadingRepository =
                statusReadingRepository;

        this.scheduledActionRepository =
                scheduledActionRepository;
    }

    public String saveStatusReading(
            UUID scheduledActionId,
            StatusReading statusReading) {

        Optional<ScheduledAction> optionalAction =
                scheduledActionRepository
                        .findById(scheduledActionId);

        if (optionalAction.isEmpty()) {

            return "ACTION_NOT_FOUND";
        }

        ScheduledAction scheduledAction =
                optionalAction.get();

        if (scheduledAction.getStatus()
                != ActionStatus.EXECUTED) {

            return "NOT_EXECUTED";
        }

        statusReading.setReadDate(
                LocalDate.now());

        statusReading.setScheduledAction(
                scheduledAction);

        statusReadingRepository.save(
                statusReading);

        return "SUCCESS";
    }

    public List<StatusReading>
            getReadingsBySmartDevice(
                    String serialNumber) {

        return statusReadingRepository
                .findBySmartDeviceSerialNumber(
                        serialNumber);
    }

    public String deleteStatusReading(
            UUID id) {

        if (!statusReadingRepository
                .existsById(id)) {

            return "NOT_FOUND";
        }

        statusReadingRepository.deleteById(id);

        return "SUCCESS";
    }
}