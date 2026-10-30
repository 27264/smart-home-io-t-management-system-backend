package com.smarthome.iotmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.smarthome.iotmanagement.model.Controller;
import com.smarthome.iotmanagement.model.Protocol;
import com.smarthome.iotmanagement.model.Room;
import com.smarthome.iotmanagement.repository.ControllerRepository;
import com.smarthome.iotmanagement.repository.ProtocolRepository;
import com.smarthome.iotmanagement.repository.RoomRepository;

@Service
public class ControllerService {

    private final ControllerRepository controllerRepository;
    private final RoomRepository roomRepository;
    private final ProtocolRepository protocolRepository;

    public ControllerService(
            ControllerRepository controllerRepository,
            RoomRepository roomRepository,
            ProtocolRepository protocolRepository) {
        this.controllerRepository = controllerRepository;
        this.roomRepository = roomRepository;
        this.protocolRepository = protocolRepository;
    }

    public Controller saveController(Controller controller) {
        if (controller.getRoom() != null && controller.getRoom().getId() != null) {
            Optional<Room> room = roomRepository.findById(controller.getRoom().getId());
            if (room.isPresent()) {
                controller.setRoom(room.get());
            }
        }
        if (controller.getProtocols() != null) {
            List<Protocol> realProtocols = new ArrayList<>();
            for (Protocol protocol : controller.getProtocols()) {
                if (protocol.getId() != null) {
                    Optional<Protocol> existingProtocol = protocolRepository.findById(protocol.getId());
                    if (existingProtocol.isPresent()) {
                        realProtocols.add(existingProtocol.get());
                    }
                }
            }
            controller.setProtocols(realProtocols);
        }
        return controllerRepository.save(controller);
    }

    public List<Controller> getAllControllers() {
        return controllerRepository.findAll();
    }

    public Optional<Controller> getControllerById(UUID id) {
        return controllerRepository.findById(id);
    }

    public Controller updateController(UUID id, Controller controllerDetails) {
        Optional<Controller> existingController = controllerRepository.findById(id);
        if (existingController.isEmpty()) {
            return null;
        }
        Controller controller = existingController.get();
        controller.setControllerName(controllerDetails.getControllerName());
        controller.setModel(controllerDetails.getModel());
        controller.setIpAddress(controllerDetails.getIpAddress());
        if (controllerDetails.getRoom() != null && controllerDetails.getRoom().getId() != null) {
            Optional<Room> room = roomRepository.findById(controllerDetails.getRoom().getId());
            if (room.isPresent()) {
                controller.setRoom(room.get());
            }
        }
        if (controllerDetails.getProtocols() != null) {
            List<Protocol> realProtocols = new ArrayList<>();
            for (Protocol protocol : controllerDetails.getProtocols()) {
                if (protocol.getId() != null) {
                    Optional<Protocol> existingProtocol = protocolRepository.findById(protocol.getId());
                    if (existingProtocol.isPresent()) {
                        realProtocols.add(existingProtocol.get());
                    }
                }
            }
            controller.setProtocols(realProtocols);
        }
        return controllerRepository.save(controller);
    }

    public String deleteController(UUID id) {
        if (!controllerRepository.existsById(id)) {
            return "The controller with that id does not exist";
        }
        controllerRepository.deleteById(id);
        return "Controller deleted successfully";
    }

    // C1
    public List<Controller> getControllersByProtocol(String name) {
        return controllerRepository.findControllersByProtocolName(name);
    }
}
