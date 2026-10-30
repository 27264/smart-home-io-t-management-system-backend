package com.smarthome.iotmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.smarthome.iotmanagement.model.ActionStatus;
import com.smarthome.iotmanagement.model.ScheduledAction;
import com.smarthome.iotmanagement.service.ScheduledActionService;

@RestController
@RequestMapping("/api/scheduled-actions")
public class ScheduledActionController {

    private final ScheduledActionService
            scheduledActionService;


    public ScheduledActionController(
            ScheduledActionService scheduledActionService) {

        this.scheduledActionService =
                scheduledActionService;
    }


    // D1 - SAFE SAVE
    @PostMapping("/save")
    public ResponseEntity<String>
            saveScheduledAction(
                    @RequestBody
                    ScheduledAction scheduledAction) {


        // Reject past dates
        if (scheduledAction
                .getActionDate()
                .isBefore(LocalDate.now())) {

            return new ResponseEntity<>(
                    "Scheduled action date cannot be in the past",
                    HttpStatus.BAD_REQUEST);
        }


        // Reject duplicate controller/date
        // if existing action is not ABORTED
        if (scheduledAction.getController() != null
                && scheduledAction
                        .getController()
                        .getId() != null) {

            boolean alreadyExists =
                    scheduledActionService
                            .controllerAlreadyHasAction(
                                    scheduledAction
                                            .getController()
                                            .getId(),

                                    scheduledAction
                                            .getActionDate());


            if (alreadyExists) {

                return new ResponseEntity<>(
                        "Controller already has an action scheduled on that date",
                        HttpStatus.CONFLICT);
            }
        }


        scheduledActionService
                .saveScheduledAction(
                        scheduledAction);


        return new ResponseEntity<>(
                "Scheduled action saved successfully",
                HttpStatus.CREATED);
    }


    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<List<ScheduledAction>>
            getAllScheduledActions() {

        return new ResponseEntity<>(
                scheduledActionService
                        .getAllScheduledActions(),
                HttpStatus.OK);
    }


    // B2 - FIND BY STATUS
    @GetMapping("/by-status")
    public ResponseEntity<List<ScheduledAction>>
            getScheduledActionsByStatus(
                    @RequestParam
                    ActionStatus status) {

        return new ResponseEntity<>(
                scheduledActionService
                        .getScheduledActionsByStatus(
                                status),
                HttpStatus.OK);
    }


    // B3 - FIND BETWEEN DATES
    @GetMapping("/between")
    public ResponseEntity<List<ScheduledAction>>
            getScheduledActionsBetweenDates(
                    @RequestParam
                    String start,

                    @RequestParam
                    String end) {


        LocalDate startDate =
                LocalDate.parse(start);

        LocalDate endDate =
                LocalDate.parse(end);


        return new ResponseEntity<>(
                scheduledActionService
                        .getScheduledActionsBetweenDates(
                                startDate,
                                endDate),
                HttpStatus.OK);
    }


    // D2 - CHANGE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<String>
            changeStatus(
                    @PathVariable
                    UUID id,

                    @RequestParam
                    ActionStatus status) {


        String result =
                scheduledActionService
                        .changeStatus(
                                id,
                                status);


        if (result.equals(
                "NOT_FOUND")) {

            return new ResponseEntity<>(
                    "The scheduled action with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }


        if (!result.equals(
                "SUCCESS")) {

            return new ResponseEntity<>(
                    result,
                    HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(
                "Scheduled action status updated successfully",
                HttpStatus.OK);
    }


    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?>
            getScheduledActionById(
                    @PathVariable
                    UUID id) {


        Optional<ScheduledAction>
                scheduledAction =
                scheduledActionService
                        .getScheduledActionById(
                                id);


        if (scheduledAction.isEmpty()) {

            return new ResponseEntity<>(
                    "The scheduled action with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity<>(
                scheduledAction.get(),
                HttpStatus.OK);
    }


    // GENERAL UPDATE
    // STATUS DOES NOT CHANGE HERE
    @PutMapping("/update/{id}")
    public ResponseEntity<?>
            updateScheduledAction(
                    @PathVariable
                    UUID id,

                    @RequestBody
                    ScheduledAction
                            scheduledActionDetails) {


        ScheduledAction scheduledAction =
                scheduledActionService
                        .updateScheduledAction(
                                id,
                                scheduledActionDetails);


        if (scheduledAction == null) {

            return new ResponseEntity<>(
                    "The scheduled action with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                scheduledAction,
                HttpStatus.OK);
    }


    // E3 - DELETE WITH STATUS
    // READING PROTECTION
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String>
            deleteScheduledAction(
                    @PathVariable
                    UUID id) {


        String result =
                scheduledActionService
                        .deleteScheduledAction(
                                id);


        if (result.equals(
                "NOT_FOUND")) {

            return new ResponseEntity<>(
                    "The scheduled action with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }


        if (result.equals(
                "HAS_READINGS")) {

            return new ResponseEntity<>(
                    "That scheduled action has status readings, delete them first",
                    HttpStatus.CONFLICT);
        }


        return new ResponseEntity<>(
                "Scheduled action deleted successfully",
                HttpStatus.OK);
    }


    // F1 - SCHEDULED ACTIONS PER STATUS
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>>
            getScheduledActionStatsByStatus() {

        return new ResponseEntity<>(
                scheduledActionService
                        .getScheduledActionStatsByStatus(),
                HttpStatus.OK);
    }


    // F2 - CONTROLLER IS ABSENT
    // CANCEL ALL NON-EXECUTED ACTIONS
    // FOR THAT CONTROLLER ON THAT DATE
    @PatchMapping("/cancel-day")
    public ResponseEntity<String>
            cancelControllerActionsForDay(
                    @RequestParam
                    UUID controllerId,

                    @RequestParam
                    String date) {


        LocalDate actionDate =
                LocalDate.parse(date);


        int cancelled =
                scheduledActionService
                        .cancelControllerActionsForDay(
                                controllerId,
                                actionDate);


        return new ResponseEntity<>(
                cancelled
                        + " scheduled actions cancelled",
                HttpStatus.OK);
    }

    // BONUS - PAGINATION
    @GetMapping("/page")
    public ResponseEntity<Page<ScheduledAction>> getScheduledActionsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "actionDate,asc") String sort) {

        String[] sortParts = sort.split(",");
        String sortProperty = sortParts[0];
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        return new ResponseEntity<>(
                scheduledActionService.getScheduledActionsPage(pageable),
                HttpStatus.OK);
    }
}
