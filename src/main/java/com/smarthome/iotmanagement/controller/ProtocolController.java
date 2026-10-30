package com.smarthome.iotmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smarthome.iotmanagement.model.Protocol;
import com.smarthome.iotmanagement.service.ProtocolService;

@RestController
@RequestMapping("/api/protocols")
public class ProtocolController {

    private final ProtocolService protocolService;

    public ProtocolController(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveProtocol(@RequestBody Protocol protocol) {
        protocolService.saveProtocol(protocol);
        return new ResponseEntity<>("Protocol saved successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Protocol>> getAllProtocols() {
        return new ResponseEntity<>(protocolService.getAllProtocols(), HttpStatus.OK);
    }

    // C3
    @GetMapping("/unused")
    public ResponseEntity<List<Protocol>> getUnusedProtocols() {
        return new ResponseEntity<>(protocolService.getUnusedProtocols(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProtocolById(@PathVariable UUID id) {
        Optional<Protocol> protocol = protocolService.getProtocolById(id);
        if (protocol.isEmpty()) {
            return new ResponseEntity<>("The protocol with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(protocol.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProtocol(@PathVariable UUID id, @RequestBody Protocol protocolDetails) {
        Protocol protocol = protocolService.updateProtocol(id, protocolDetails);
        if (protocol == null) {
            return new ResponseEntity<>("The protocol with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(protocol, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProtocol(@PathVariable UUID id) {
        String message = protocolService.deleteProtocol(id);
        if (message.equals("The protocol with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
