package com.smarthome.iotmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smarthome.iotmanagement.model.SmartDevice;
import com.smarthome.iotmanagement.service.SmartDeviceService;

@RestController
@RequestMapping("/api/smart-devices")
public class SmartDeviceController {

    private final SmartDeviceService smartDeviceService;

    public SmartDeviceController(SmartDeviceService smartDeviceService) {
        this.smartDeviceService = smartDeviceService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveSmartDevice(@RequestBody SmartDevice smartDevice) {
        smartDeviceService.saveSmartDevice(smartDevice);
        return new ResponseEntity<>("Smart device saved successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SmartDevice>> getAllSmartDevices() {
        return new ResponseEntity<>(smartDeviceService.getAllSmartDevices(), HttpStatus.OK);
    }

    // B1
    @GetMapping("/by-brand")
    public ResponseEntity<List<SmartDevice>> getSmartDevicesByBrand(@RequestParam String brand) {
        return new ResponseEntity<>(smartDeviceService.getSmartDevicesByBrand(brand), HttpStatus.OK);
    }

    // C4
    @GetMapping("/of-controller/{controllerId}")
    public ResponseEntity<?> getDevicesOfController(@PathVariable UUID controllerId) {
        if (!smartDeviceService.controllerExists(controllerId)) {
            return new ResponseEntity<>("The controller with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(smartDeviceService.getSmartDevicesOfController(controllerId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSmartDeviceById(@PathVariable UUID id) {
        Optional<SmartDevice> smartDevice = smartDeviceService.getSmartDeviceById(id);
        if (smartDevice.isEmpty()) {
            return new ResponseEntity<>("The smart device with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(smartDevice.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSmartDevice(@PathVariable UUID id, @RequestBody SmartDevice smartDeviceDetails) {
        SmartDevice smartDevice = smartDeviceService.updateSmartDevice(id, smartDeviceDetails);
        if (smartDevice == null) {
            return new ResponseEntity<>("The smart device with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(smartDevice, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSmartDevice(@PathVariable UUID id) {
        String message = smartDeviceService.deleteSmartDevice(id);
        if (message.equals("The smart device with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
