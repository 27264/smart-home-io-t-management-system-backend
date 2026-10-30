package com.smarthome.iotmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smarthome.iotmanagement.model.Room;
import com.smarthome.iotmanagement.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveRoom(@RequestBody Room room) {
        roomService.saveRoom(room);
        return new ResponseEntity<>("Room saved successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable UUID id) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isEmpty()) {
            return new ResponseEntity<>("The room with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(room.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable UUID id, @RequestBody Room roomDetails) {
        Room room = roomService.updateRoom(id, roomDetails);
        if (room == null) {
            return new ResponseEntity<>("The room with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable UUID id) {
        String message = roomService.deleteRoom(id);
        if (message.equals("The room with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
