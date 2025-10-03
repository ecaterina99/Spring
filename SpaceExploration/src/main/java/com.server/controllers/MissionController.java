package com.server.controllers;

import com.server.dto.AstronautDTO;
import com.server.dto.MissionDTO;
import com.server.dto.MissionSpecializationDTO;
import com.server.models.Mission;
import com.server.models.MissionSpecialization;
import com.server.services.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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
            @ApiResponse(responseCode = "200", description = "Mission found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Mission not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<MissionDTO> getMission(@Parameter(description = "ID of mission to retrieve", required = true, example = "1")
                                                 @PathVariable @Min(1) int id) {
        MissionDTO mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }
    @GetMapping()
    @Operation(summary = "Retrieve all missions with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all missions",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MissionDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<MissionDTO>> getAllMissions(
            @RequestParam(required = false) Mission.DifficultyLevel difficultyLevel,
            @RequestParam(required = false) Integer destinationId) {
        return ResponseEntity.ok(
                missionService.getMissionsByFilters(difficultyLevel, destinationId)
        );
    }
    @GetMapping("/by-destination/{id}")
    @Operation(summary = "Retrieve all missions by destination id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all missions by destinantion id",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AstronautDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<List<MissionDTO>> getAllMissionsByDestinationId(@PathVariable("id") int destinationId) {
        return ResponseEntity.ok(missionService.getMissionsByDestinationId(destinationId));
    }

    @PostMapping
    @Operation(summary = "Create a new mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mission created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
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
            @ApiResponse(responseCode = "200", description = "Mission updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AstronautDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Mission not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Mission code already exists for another mission"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<MissionDTO> partialUpdateMission(
            @PathVariable @Min(1) int id,
            @Valid @RequestBody MissionDTO.MissionUpdateDTO missionDTO) {
        MissionDTO updatedMission = missionService.updateMission(missionDTO, id);
        return ResponseEntity.ok(updatedMission);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mission deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Mission not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteMission(@Parameter(description = "ID of mission to delete", required = true, example = "1")
                                              @PathVariable @Min(1) int id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{missionId}/specializations")
    @Operation(summary = "Add or update  mission specializations",
            description = "Use quantity > 0 to add/update specializations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specializations added/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Mission not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Total specialists exceed crew size limit"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<MissionDTO> updateSpecializations(
            @PathVariable @Min(1) int missionId,
            @Valid @RequestBody List<MissionSpecializationDTO.AddSpecializationRequest> specializations) {

        MissionDTO updatedMission = missionService.addOrUpdateSpecializations(missionId, specializations);
        return ResponseEntity.ok(updatedMission);
    }


    @DeleteMapping("/{missionId}/delete-specialization")
    @Operation(summary = "Delete specialization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Specialization deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Mission not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteSpecialization(@PathVariable @Min(1) int missionId, @Valid @RequestBody MissionSpecializationDTO.AddSpecializationRequest request) {
        missionService.removeSpecialization(missionId, MissionSpecialization.Specialization.valueOf(request.getSpecialization()));
        return ResponseEntity.noContent().build();
    }


}


