package com.smarthome.iotmanagement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.smarthome.iotmanagement.model.Room;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    // C2
    @Query("""
            SELECT r
            FROM Room r
            WHERE NOT EXISTS (
                SELECT c
                FROM Controller c
                WHERE c.room = r
            )
            ORDER BY r.roomNumber ASC
            """)
    List<Room> findFreeRooms();

    // D4 - DERIVED
    Optional<Room> findByRoomNumber(int roomNumber);
}
