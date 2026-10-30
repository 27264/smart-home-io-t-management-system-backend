package com.smarthome.iotmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.smarthome.iotmanagement.model.SmartDevice;
import com.smarthome.iotmanagement.repository.ControllerRepository;
import com.smarthome.iotmanagement.repository.SmartDeviceRepository;

@Service
public class SmartDeviceService {

    private final SmartDeviceRepository smartDeviceRepository;
    private final ControllerRepository controllerRepository;

    public SmartDeviceService(
            SmartDeviceRepository smartDeviceRepository,
            ControllerRepository controllerRepository) {
        this.smartDeviceRepository = smartDeviceRepository;
        this.controllerRepository = controllerRepository;
    }

    public SmartDevice saveSmartDevice(SmartDevice smartDevice) {
        return smartDeviceRepository.save(smartDevice);
    }

    public List<SmartDevice> getAllSmartDevices() {
        return smartDeviceRepository.findAll();
    }

    public Optional<SmartDevice> getSmartDeviceById(UUID id) {
        return smartDeviceRepository.findById(id);
    }

    public SmartDevice updateSmartDevice(UUID id, SmartDevice smartDeviceDetails) {
        Optional<SmartDevice> existingSmartDevice = smartDeviceRepository.findById(id);
        if (existingSmartDevice.isEmpty()) {
            return null;
        }
        SmartDevice smartDevice = existingSmartDevice.get();
        smartDevice.setDeviceName(smartDeviceDetails.getDeviceName());
        smartDevice.setBrand(smartDeviceDetails.getBrand());
        smartDevice.setSerialNumber(smartDeviceDetails.getSerialNumber());
        smartDevice.setLocation(smartDeviceDetails.getLocation());
        return smartDeviceRepository.save(smartDevice);
    }

    public String deleteSmartDevice(UUID id) {
        if (!smartDeviceRepository.existsById(id)) {
            return "The smart device with that id does not exist";
        }
        smartDeviceRepository.deleteById(id);
        return "Smart device deleted successfully";
    }

    // B1 - DERIVED
    public List<SmartDevice> getSmartDevicesByBrand(String brand) {
        return smartDeviceRepository.findByBrandIgnoreCaseOrderByDeviceNameAsc(brand);
    }

    // C4
    public List<SmartDevice> getSmartDevicesOfController(UUID controllerId) {
        return smartDeviceRepository.findDevicesOfController(controllerId);
    }

    public boolean controllerExists(UUID controllerId) {
        return controllerRepository.existsById(controllerId);
    }
}
