package com.smarthome.iotmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.model.ScheduledAction;
import com.smarthome.iotmanagement.service.ScheduledActionService;

@RestController
@RequestMapping("/api/scheduled-actions")
public class ScheduledActionController {

    private final ScheduledActionService scheduledActionService;

    public ScheduledActionController(ScheduledActionService scheduledActionService) {
        this.scheduledActionService = scheduledActionService;
    }

    // D1 - SAFE SAVE
    @PostMapping("/save")
    public ResponseEntity<String> saveScheduledAction(@RequestBody ScheduledAction scheduledAction) {
        if (scheduledAction.getActionDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Scheduled action date cannot be in the past", HttpStatus.BAD_REQUEST);
        }
        if (scheduledAction.getController() != null && scheduledAction.getController().getId() != null) {
            boolean alreadyExists = scheduledActionService.controllerAlreadyHasAction(
                    scheduledAction.getController().getId(), scheduledAction.getActionDate());
            if (alreadyExists) {
                return new ResponseEntity<>("Controller already has an action scheduled on that date", HttpStatus.CONFLICT);
            }
        }
        scheduledActionService.saveScheduledAction(scheduledAction);
        return new ResponseEntity<>("Scheduled action saved successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ScheduledAction>> getAllScheduledActions() {
        return new ResponseEntity<>(scheduledActionService.getAllScheduledActions(), HttpStatus.OK);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<ScheduledAction>> getScheduledActionsByStatus(@RequestParam ActionStatus status) {
        return new ResponseEntity<>(scheduledActionService.getScheduledActionsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/between")
    public ResponseEntity<List<ScheduledAction>> getScheduledActionsBetweenDates(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(scheduledActionService.getScheduledActionsBetweenDates(startDate, endDate), HttpStatus.OK);
    }

    // D2 - CHANGE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable UUID id, @RequestParam ActionStatus status) {
        String result = scheduledActionService.changeStatus(id, status);
        if (result.equals("NOT_FOUND")) {
            return new ResponseEntity<>("The scheduled action with that id does not exist", HttpStatus.NOT_FOUND);
        }
        if (!result.equals("SUCCESS")) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Scheduled action status updated successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScheduledActionById(@PathVariable UUID id) {
        Optional<ScheduledAction> scheduledAction = scheduledActionService.getScheduledActionById(id);
        if (scheduledAction.isEmpty()) {
            return new ResponseEntity<>("The scheduled action with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduledAction.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateScheduledAction(@PathVariable UUID id, @RequestBody ScheduledAction scheduledActionDetails) {
        ScheduledAction scheduledAction = scheduledActionService.updateScheduledAction(id, scheduledActionDetails);
        if (scheduledAction == null) {
            return new ResponseEntity<>("The scheduled action with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduledAction, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteScheduledAction(@PathVariable UUID id) {
        String message = scheduledActionService.deleteScheduledAction(id);
        if (message.equals("The scheduled action with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
