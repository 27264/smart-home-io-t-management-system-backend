package com.smarthome.iotmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smarthome.iotmanagement.model.Controller;
import com.smarthome.iotmanagement.service.ControllerService;

@RestController
@RequestMapping("/api/controllers")
public class ControllerController {

    private final ControllerService controllerService;

    public ControllerController(
            ControllerService controllerService) {

        this.controllerService =
                controllerService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveController(
            @RequestBody Controller controller) {

        controllerService.saveController(controller);

        return new ResponseEntity<>(
                "Controller saved successfully",
                HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Controller>>
            getAllControllers() {

        return new ResponseEntity<>(
                controllerService.getAllControllers(),
                HttpStatus.OK);
    }

    // C1
    @GetMapping("/by-protocol")
    public ResponseEntity<List<Controller>>
            getControllersByProtocol(
                    @RequestParam String name) {

        return new ResponseEntity<>(
                controllerService
                        .getControllersByProtocol(name),
                HttpStatus.OK);
    }

    // D3
    @PutMapping("/{controllerId}/protocols/{protocolId}")
    public ResponseEntity<String> addProtocol(
            @PathVariable UUID controllerId,
            @PathVariable UUID protocolId) {

        String result =
                controllerService.addProtocol(
                        controllerId,
                        protocolId);

        if (result.equals(
                "CONTROLLER_NOT_FOUND")) {

            return new ResponseEntity<>(
                    "The controller with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        if (result.equals(
                "PROTOCOL_NOT_FOUND")) {

            return new ResponseEntity<>(
                    "That protocol does not exist",
                    HttpStatus.NOT_FOUND);
        }

        if (result.equals(
                "ALREADY_HAS_PROTOCOL")) {

            return new ResponseEntity<>(
                    "The controller already has that protocol",
                    HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                "Protocol added to the controller",
                HttpStatus.OK);
    }

    // D4
    @PatchMapping("/{controllerId}/room")
    public ResponseEntity<String> changeRoom(
            @PathVariable UUID controllerId,
            @RequestParam int roomNumber) {

        String result =
                controllerService.changeRoom(
                        controllerId,
                        roomNumber);

        if (result.equals(
                "CONTROLLER_NOT_FOUND")) {

            return new ResponseEntity<>(
                    "The controller with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        if (result.equals(
                "ROOM_NOT_FOUND")) {

            return new ResponseEntity<>(
                    "The room with that roomNumber does not exist",
                    HttpStatus.NOT_FOUND);
        }

        if (result.equals(
                "ROOM_TAKEN")) {

            return new ResponseEntity<>(
                    "That room is taken by another controller",
                    HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                "Room changed successfully",
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getControllerById(
            @PathVariable UUID id) {

        Optional<Controller> controller =
                controllerService
                        .getControllerById(id);

        if (controller.isEmpty()) {

            return new ResponseEntity<>(
                    "The controller with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                controller.get(),
                HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateController(
            @PathVariable UUID id,
            @RequestBody Controller controllerDetails) {

        Controller controller =
                controllerService
                        .updateController(
                                id,
                                controllerDetails);

        if (controller == null) {

            return new ResponseEntity<>(
                    "The controller with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                controller,
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteController(
            @PathVariable UUID id) {

        String message =
                controllerService
                        .deleteController(id);

        if (message.equals(
                "The controller with that id does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.OK);
    }
}
