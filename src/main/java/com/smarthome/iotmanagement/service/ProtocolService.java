package com.smarthome.iotmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.smarthome.iotmanagement.model.Protocol;
import com.smarthome.iotmanagement.repository.ProtocolRepository;

@Service
public class ProtocolService {

    private final ProtocolRepository protocolRepository;

    public ProtocolService(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    public Protocol saveProtocol(Protocol protocol) {
        return protocolRepository.save(protocol);
    }

    public List<Protocol> getAllProtocols() {
        return protocolRepository.findAll();
    }

    // C3
    public List<Protocol> getUnusedProtocols() {
        return protocolRepository.findUnusedProtocols();
    }

    public Optional<Protocol> getProtocolById(UUID id) {
        return protocolRepository.findById(id);
    }

    public Protocol updateProtocol(UUID id, Protocol protocolDetails) {
        Optional<Protocol> existingProtocol = protocolRepository.findById(id);
        if (existingProtocol.isEmpty()) {
            return null;
        }
        Protocol protocol = existingProtocol.get();
        protocol.setName(protocolDetails.getName());
        return protocolRepository.save(protocol);
    }

    public String deleteProtocol(UUID id) {
        if (!protocolRepository.existsById(id)) {
            return "The protocol with that id does not exist";
        }
        protocolRepository.deleteById(id);
        return "Protocol deleted successfully";
    }
}
