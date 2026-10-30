package com.smarthome.iotmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smarthome.iotmanagement.model.Controller;
import com.smarthome.iotmanagement.service.ControllerService;

@RestController
@RequestMapping("/api/controllers")
public class ControllerController {

    private final ControllerService controllerService;

    public ControllerController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveController(@RequestBody Controller controller) {
        controllerService.saveController(controller);
        return new ResponseEntity<>("Controller saved successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Controller>> getAllControllers() {
        return new ResponseEntity<>(controllerService.getAllControllers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getControllerById(@PathVariable UUID id) {
        Optional<Controller> controller = controllerService.getControllerById(id);
        if (controller.isEmpty()) {
            return new ResponseEntity<>("The controller with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(controller.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateController(@PathVariable UUID id, @RequestBody Controller controllerDetails) {
        Controller controller = controllerService.updateController(id, controllerDetails);
        if (controller == null) {
            return new ResponseEntity<>("The controller with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(controller, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteController(@PathVariable UUID id) {
        String message = controllerService.deleteController(id);
        if (message.equals("The controller with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
