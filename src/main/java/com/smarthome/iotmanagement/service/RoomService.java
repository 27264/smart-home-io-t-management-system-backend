package com.smarthome.iotmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.smarthome.iotmanagement.model.Room;
import com.smarthome.iotmanagement.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(UUID id) {
        return roomRepository.findById(id);
    }

    public Room updateRoom(UUID id, Room roomDetails) {
        Optional<Room> existingRoom = roomRepository.findById(id);
        if (existingRoom.isEmpty()) {
            return null;
        }
        Room room = existingRoom.get();
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setName(roomDetails.getName());
        return roomRepository.save(room);
    }

    public String deleteRoom(UUID id) {
        if (!roomRepository.existsById(id)) {
            return "The room with that id does not exist";
        }
        roomRepository.deleteById(id);
        return "Room deleted successfully";
    }

    // C2
    public List<Room> getFreeRooms() {
        return roomRepository.findFreeRooms();
    }
}
