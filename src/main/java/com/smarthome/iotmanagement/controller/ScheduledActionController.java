package com.smarthome.iotmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smarthome.iotmanagement.model.ScheduledAction;
import com.smarthome.iotmanagement.service.ScheduledActionService;

@RestController
@RequestMapping("/api/scheduled-actions")
public class ScheduledActionController {

    private final ScheduledActionService scheduledActionService;

    public ScheduledActionController(ScheduledActionService scheduledActionService) {
        this.scheduledActionService = scheduledActionService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveScheduledAction(@RequestBody ScheduledAction scheduledAction) {
        scheduledActionService.saveScheduledAction(scheduledAction);
        return new ResponseEntity<>("Scheduled action saved successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ScheduledAction>> getAllScheduledActions() {
        return new ResponseEntity<>(scheduledActionService.getAllScheduledActions(), HttpStatus.OK);
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
