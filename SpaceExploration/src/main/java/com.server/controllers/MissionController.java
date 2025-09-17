package com.server.controllers;

import com.server.dto.MissionDTO;
import com.server.services.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missions")
@Tag(name = "Missions", description = "Missions with different level of difficulty")

public class MissionController {
    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve mission by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission found"),
            @ApiResponse(responseCode = "404", description = "Mission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<MissionDTO> getMission(@PathVariable int id) {
        MissionDTO mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    @GetMapping()
    @Operation(summary = "Retrieve all missions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all missions"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<List<MissionDTO>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @PostMapping
    @Operation(summary = "Create a new mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mission created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Mission code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<MissionDTO> addMission(@Valid @RequestBody MissionDTO missionDTO) {
        MissionDTO createdMission = missionService.addMission(missionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMission);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update mission partially")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission updated successfully"),
            @ApiResponse(responseCode = "404", description = "Mission not found"),
            @ApiResponse(responseCode = "409", description = "Mission code already exists for another mission"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<MissionDTO> partialUpdateMission(
            @PathVariable int id,
            @RequestBody MissionDTO missionDTO) {
        MissionDTO updatedMission = missionService.updateMission(missionDTO, id);
        return ResponseEntity.ok(updatedMission);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Mission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteMission(@PathVariable int id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
}
