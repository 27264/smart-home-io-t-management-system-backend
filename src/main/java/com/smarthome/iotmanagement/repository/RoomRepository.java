package com.smarthome.iotmanagement.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smarthome.iotmanagement.model.Room;

public interface RoomRepository extends JpaRepository<Room, UUID> {
}
