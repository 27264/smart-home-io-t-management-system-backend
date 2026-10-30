package com.smarthome.iotmanagement.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.smarthome.iotmanagement.model.Controller;

public interface ControllerRepository extends JpaRepository<Controller, UUID> {

    // C1
    @Query("""
            SELECT DISTINCT c
            FROM Controller c
            JOIN c.protocols p
            WHERE LOWER(p.name) = LOWER(:name)
            """)
    List<Controller> findControllersByProtocolName(@Param("name") String name);
}
