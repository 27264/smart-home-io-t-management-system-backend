package com.smarthome.iotmanagement.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "controller")
public class Controller {

    @Id
    @GeneratedValue
    private UUID id;

    private String controllerName;

    private String model;

    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToMany
    @JoinTable(
        name = "controller_protocol",
        joinColumns = @JoinColumn(name = "controller_id"),
        inverseJoinColumns = @JoinColumn(name = "protocol_id")
    )
    private List<Protocol> protocols = new ArrayList<>();

    public Controller() {
    }

    public Controller(
            String controllerName,
            String model,
            String ipAddress,
            Room room) {

        this.controllerName = controllerName;
        this.model = model;
        this.ipAddress = ipAddress;
        this.room = room;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Protocol> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<Protocol> protocols) {
        this.protocols = protocols;
    }
}
