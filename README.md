# Smart Home IoT Management System Backend

**Academic Year:** 2026-2027 (1)  
**Course:** INSY 8322 Web Technology and Internet  
**Lecturer:** Patrick DUSHIMIMANA  
**Institution:** Adventist University of Central Africa (AUCA)  

---

### Student Details
- **Student Name:** BIGWI RURANGWA Kenny
- **Student ID:** 27264
- **Project Name:** Smart Home IoT Management System
- **Repository:** [https://github.com/27264/smart-home-io-t-management-system-backend](https://github.com/27264/smart-home-io-t-management-system-backend)

---

## 1. Project Overview

The **Smart Home IoT Management System** is a backend RESTful service developed using **Spring Boot**, **Spring Data JPA**, and **PostgreSQL**. The platform enables household monitoring and automated control of connected smart devices through room controllers across multiple wireless protocols (WiFi, Zigbee, Bluetooth, Z-Wave). 

Scheduled actions proceed through structured lifecycle stages (`PLANNED` -> `QUEUED` -> `EXECUTED` / `ABORTED`). Once an action is successfully executed, the system captures real-time status readings reported by the devices.

---

## 2. Domain Entities & Architecture

| Entity | Table | Key Fields & Annotations | Relationships |
| :--- | :--- | :--- | :--- |
| **Protocol** | `protocol` | `UUID id`, `String name` | `@ManyToMany(mappedBy = "protocols")` marked with `@JsonIgnore` |
| **Room** | `room` | `UUID id`, `int roomNumber`, `String name` | Unidirectional (Room has no controller reference) |
| **Controller** | `controller` | `UUID id`, `String controllerName`, `String model`, `String ipAddress` | `@ManyToOne Room room` (`room_id`), `@ManyToMany List<Protocol> protocols` via join table `controller_protocol` |
| **SmartDevice** | `smart_device` | `UUID id`, `String deviceName`, `String brand`, `String serialNumber` (unique), `String location` | Independent device entity (`serialNumber` is business key) |
| **ScheduledAction** | `scheduled_action` | `UUID id`, `LocalDate actionDate`, `@Enumerated(EnumType.STRING) ActionStatus status`, `String command` | `@ManyToOne SmartDevice smartDevice`, `@ManyToOne Controller controller` |
| **StatusReading** | `status_reading` | `UUID id`, `String metric`, `String note`, `int value`, `LocalDate readDate` | `@ManyToOne ScheduledAction scheduledAction` (`scheduled_action_id`) |

---

## 3. Prerequisites

- **Java JDK:** 17 or 21+
- **Database:** PostgreSQL 14+
- **Build Tool:** Maven (or the included `./mvnw` wrapper)
- **API Testing:** Postman

---

## 4. Setup & Running the Application

### 1. Database Creation
Create a PostgreSQL database named `smarthome_iot`:
```sql
CREATE DATABASE smarthome_iot;
```

### 2. Configure `application.properties`
Review or update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smarthome_iot
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8082
spring.application.name=iotmanagement
```

### 3. Run the Application
From the project root directory, run:
```bash
# On Linux/macOS
./mvnw spring-boot:run

# On Windows (Command Prompt or PowerShell)
.\mvnw.cmd spring-boot:run
```
The server will start at `http://localhost:8082`.

---

## 5. Seed Data (Step 0)

To verify all queries and requirements, enter initial seed data via the provided endpoints:
1. **Protocols:** 4 protocols (`WiFi`, `Zigbee`, `Bluetooth`, `Z-Wave`). Keep `Z-Wave` unassigned.
2. **Rooms:** 4 rooms numbered 1, 2, 3, 4. Keep Room 4 free (no controller assigned).
3. **Controllers:** 3 controllers, each assigned to a room (Rooms 1, 2, 3). Assign two protocols to one controller and one protocol to another.
4. **Smart Devices:** 5 smart devices. At least two devices with brand `Xiaomi`. Use serial number `SN-7F3A9C21` for testing.
5. **Scheduled Actions:** 10 scheduled actions across October and November 2026 covering `PLANNED`, `QUEUED`, `EXECUTED`, and `ABORTED`.
6. **Status Readings:** Record status readings for the `EXECUTED` scheduled actions.

---

## 6. API Endpoints Reference

### Part A: Domain Model & CRUD (15 Marks)
- `POST /api/{path}/save` - 201 Created on success
- `GET /api/{path}/all` - 200 OK with list of entities
- `GET /api/{path}/{id}` - 200 OK or 404 Not Found
- `PUT /api/{path}/update/{id}` - 200 OK or 404 Not Found
- `DELETE /api/{path}/delete/{id}` - 200 OK or 404 Not Found  
*Where `{path}` is `protocols`, `rooms`, `controllers`, `smart-devices`, or `scheduled-actions`.*

### Part B: Derived Queries (15 Marks)
- `GET /api/smart-devices/by-brand?brand=xiaomi` (B1 - Case-insensitive, ordered by `deviceName` ASC)
- `GET /api/scheduled-actions/by-status?status=PLANNED` (B2 - Ordered by `actionDate` ASC)
- `GET /api/scheduled-actions/between?start=2026-10-01&end=2026-10-31` (B3 - Ordered by `actionDate` ASC)

### Part C: JPQL with Joins (20 Marks)
- `GET /api/controllers/by-protocol?name=wifi` (C1 - JPQL JOIN on `protocols` collection, case-insensitive)
- `GET /api/rooms/free` (C2 - Free rooms where no controller is assigned, ordered by `roomNumber` ASC)
- `GET /api/protocols/unused` (C3 - Protocols without controllers using `IS EMPTY`)
- `GET /api/smart-devices/of-controller/{controllerId}` (C4 - Distinct smart devices linked to controller via actions)

### Part D: Business Rules (25 Marks)
- `POST /api/scheduled-actions/save` (D1 - Rejects past dates with 400; rejects duplicate controller/date with 409; forces status `PLANNED`)
- `PATCH /api/scheduled-actions/{id}/status?status=QUEUED` (D2 - Enforces lifecycle: `PLANNED` -> `QUEUED`/`ABORTED`, `QUEUED` -> `EXECUTED`/`ABORTED`)
- `PUT /api/controllers/{controllerId}/protocols/{protocolId}` (D3 - Assigns protocol to controller with duplicate check: 404 / 409 / 200)
- `PATCH /api/controllers/{controllerId}/room?roomNumber=4` (D4 - Moves controller to another room with occupancy validation: 404 / 409 / 200)

### Part E: Status Readings (15 Marks)
- `POST /api/status-readings/save?scheduledActionId=...` (E1 - Only allowed for `EXECUTED` actions; sets `readDate` to today)
- `GET /api/status-readings/smart-device/{serialNumber}` (E2 - Device reading history, ordered by `readDate` DESC)
- `DELETE /api/status-readings/delete/{id}` (E3 - Deletes reading: 404 / 200)
- `DELETE /api/scheduled-actions/delete/{id}` (E3 - Protected: returns 409 if status readings exist)

### Part F: Reports & Bulk Updates (10 Marks)
- `GET /api/scheduled-actions/stats/by-status` (F1 - JPQL GROUP BY count per status)
- `PATCH /api/scheduled-actions/cancel-day?controllerId=...&date=2026-10-20` (F2 - Bulk aborts all non-executed actions for controller on given date)

### Bonus Features (+10 Marks)
- `GET /api/dashboard/summary` (+5 - Returns summary metrics: `smartDevices`, `controllers`, `freeRooms`, `actionsToday`, `planned`, `statusReadings`)
- `GET /api/scheduled-actions/page?page=0&size=5&sort=actionDate,desc` (+5 - Pagination with total elements and pages)

---

## 7. Postman Collection

The exported Postman collection is located at the root of the project:  
`smart-home-io-t-management-system-midterm.postman_collection.json`

Import this collection into Postman. It contains individual requests for every requirement named by its ID (`A2`, `B1`, `B2`, `B3`, `C1`, `C2`, `C3`, `C4`, `D1`, `D2`, `D3`, `D4`, `E1`, `E2`, `E3`, `F1`, `F2`, `Bonus`).

