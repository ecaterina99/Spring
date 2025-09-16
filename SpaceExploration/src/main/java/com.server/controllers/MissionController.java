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
    @Operation(summary = "Get mission by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission found"),
            @ApiResponse(responseCode = "404", description = "Mission not found"),

    })
    public ResponseEntity<MissionDTO> getMission(@PathVariable int id) {
        MissionDTO mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    @GetMapping()
    @Operation(summary = "Get all missions")
    public ResponseEntity<List<MissionDTO>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mission created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Mission code already exists")
    })
    public ResponseEntity<MissionDTO> addMission(@Valid @RequestBody MissionDTO missionDTO) {
        MissionDTO createdMission = missionService.addMission(missionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMission);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update an existing mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Mission not found"),
            @ApiResponse(responseCode = "409", description = "Mission code already exists for another mission")
    })
    public ResponseEntity<MissionDTO> partialUpdateMission(
            @PathVariable int id,
            @RequestBody MissionDTO missionDTO) {
        MissionDTO updatedMission = missionService.updateMission(missionDTO, id);
        return ResponseEntity.ok(updatedMission);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Mission not found"),
    })
    public ResponseEntity<Void> deleteMission(@PathVariable int id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
}
