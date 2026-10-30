package com.smarthome.iotmanagement.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "status_reading")
public class StatusReading {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String metric;

    private String note;

    private int value;

    private LocalDate readDate;

    @ManyToOne
    @JoinColumn(name = "scheduled_action_id")
    private ScheduledAction scheduledAction;

    public StatusReading() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDate getReadDate() {
        return readDate;
    }

    public void setReadDate(LocalDate readDate) {
        this.readDate = readDate;
    }

    public ScheduledAction getScheduledAction() {
        return scheduledAction;
    }

    public void setScheduledAction(
            ScheduledAction scheduledAction) {

        this.scheduledAction = scheduledAction;
    }
}