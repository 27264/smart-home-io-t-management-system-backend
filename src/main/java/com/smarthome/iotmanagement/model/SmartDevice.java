package com.smarthome.iotmanagement.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "smart_device")
public class SmartDevice {

    @Id
    @GeneratedValue
    private UUID id;

    private String deviceName;

    private String brand;

    @Column(unique = true)
    private String serialNumber;

    private String location;

    public SmartDevice() {
    }

    public SmartDevice(
            String deviceName,
            String brand,
            String serialNumber,
            String location) {

        this.deviceName = deviceName;
        this.brand = brand;
        this.serialNumber = serialNumber;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
