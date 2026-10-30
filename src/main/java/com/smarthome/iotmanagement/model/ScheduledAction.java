package com.smarthome.iotmanagement.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "scheduled_action")
public class ScheduledAction {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate actionDate;

    @Enumerated(EnumType.STRING)
    private ActionStatus status;

    private String command;

    @ManyToOne
    @JoinColumn(name = "smart_device_id")
    private SmartDevice smartDevice;

    @ManyToOne
    @JoinColumn(name = "controller_id")
    private Controller controller;

    public ScheduledAction() {
    }

    public ScheduledAction(
            LocalDate actionDate,
            ActionStatus status,
            String command,
            SmartDevice smartDevice,
            Controller controller) {

        this.actionDate = actionDate;
        this.status = status;
        this.command = command;
        this.smartDevice = smartDevice;
        this.controller = controller;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    public ActionStatus getStatus() {
        return status;
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public SmartDevice getSmartDevice() {
        return smartDevice;
    }

    public void setSmartDevice(SmartDevice smartDevice) {
        this.smartDevice = smartDevice;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
