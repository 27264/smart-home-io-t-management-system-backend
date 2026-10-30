package com.smarthome.iotmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smarthome.iotmanagement.model.StatusReading;
import com.smarthome.iotmanagement.service.StatusReadingService;

@RestController
@RequestMapping("/api/status-readings")
public class StatusReadingController {

    private final StatusReadingService
            statusReadingService;

    public StatusReadingController(
            StatusReadingService statusReadingService) {

        this.statusReadingService =
                statusReadingService;
    }

    // E1
    @PostMapping("/save")
    public ResponseEntity<String> saveStatusReading(
            @RequestParam UUID scheduledActionId,
            @RequestBody StatusReading statusReading) {

        String result =
                statusReadingService
                        .saveStatusReading(
                                scheduledActionId,
                                statusReading);

        if (result.equals("ACTION_NOT_FOUND")) {

            return new ResponseEntity<>(
                    "The scheduled action with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        if (result.equals("NOT_EXECUTED")) {

            return new ResponseEntity<>(
                    "Status readings are only for EXECUTED scheduled actions",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                "Status reading saved successfully",
                HttpStatus.CREATED);
    }

    // E2
    @GetMapping("/smart-device/{serialNumber}")
    public ResponseEntity<List<StatusReading>>
            getReadingsBySmartDevice(
                    @PathVariable String serialNumber) {

        return new ResponseEntity<>(
                statusReadingService
                        .getReadingsBySmartDevice(
                                serialNumber),
                HttpStatus.OK);
    }

    // E3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String>
            deleteStatusReading(
                    @PathVariable UUID id) {

        String result =
                statusReadingService
                        .deleteStatusReading(id);

        if (result.equals("NOT_FOUND")) {

            return new ResponseEntity<>(
                    "The status reading with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                "Status reading deleted successfully",
                HttpStatus.OK);
    }
}
