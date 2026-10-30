package com.smarthome.iotmanagement.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.repository.ControllerRepository;
import com.smarthome.iotmanagement.repository.RoomRepository;
import com.smarthome.iotmanagement.repository.ScheduledActionRepository;
import com.smarthome.iotmanagement.repository.SmartDeviceRepository;
import com.smarthome.iotmanagement.repository.StatusReadingRepository;

@Service
public class DashboardService {

    private final SmartDeviceRepository smartDeviceRepository;
    private final ControllerRepository controllerRepository;
    private final RoomRepository roomRepository;
    private final ScheduledActionRepository scheduledActionRepository;
    private final StatusReadingRepository statusReadingRepository;

    public DashboardService(
            SmartDeviceRepository smartDeviceRepository,
            ControllerRepository controllerRepository,
            RoomRepository roomRepository,
            ScheduledActionRepository scheduledActionRepository,
            StatusReadingRepository statusReadingRepository) {

        this.smartDeviceRepository = smartDeviceRepository;
        this.controllerRepository = controllerRepository;
        this.roomRepository = roomRepository;
        this.scheduledActionRepository = scheduledActionRepository;
        this.statusReadingRepository = statusReadingRepository;
    }

    public Map<String, Long> getDashboardSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("smartDevices", smartDeviceRepository.count());
        summary.put("controllers", controllerRepository.count());
        summary.put("freeRooms", (long) roomRepository.findFreeRooms().size());
        summary.put("actionsToday", scheduledActionRepository.countByActionDate(LocalDate.now()));
        summary.put("planned", scheduledActionRepository.countByStatus(ActionStatus.PLANNED));
        summary.put("statusReadings", statusReadingRepository.count());
        return summary;
    }
}

