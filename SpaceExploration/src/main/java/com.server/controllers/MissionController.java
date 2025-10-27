package com.server.controllers;

import com.server.dto.*;
import com.server.models.Mission;
import com.server.models.MissionSpecialization;
import com.server.models.User;
import com.server.repositories.UserRepository;
import com.server.services.*;
import com.server.dto.MissionResultDTO;
import com.server.dto.MissionStartResponseDTO;
import com.server.util.GlobalApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missions")
@Tag(name = "Missions", description = "Missions with different level of difficulty")
@SecurityRequirement(name = "Bearer Authentication")

public class MissionController implements GlobalApiResponses {
    private final MissionService missionService;
    private final MissionParticipantsService missionParticipantsService;
    private final MissionReportService missionReportService;
    private final BudgetService budgetService;
    private final UserRepository userRepository;

    public MissionController(MissionService missionService, MissionParticipantsService missionParticipantsService, MissionReportService missionReportService, BudgetService budgetService, UserRepository userRepository) {
        this.missionService = missionService;
        this.missionParticipantsService = missionParticipantsService;
        this.missionReportService = missionReportService;
        this.budgetService = budgetService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve mission by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)))
    })
    public ResponseEntity<MissionDTO> getMission(@Parameter(description = "ID of mission to retrieve", required = true, example = "1")
                                                 @PathVariable @Min(1) int id) {
        MissionDTO mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    @GetMapping()
    @Operation(summary = "Retrieve all missions with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(  responseCode = "200",
                    description = "Successfully retrieved all missions",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MissionDTO.class))
                    )
            )
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
                            array = @ArraySchema(schema = @Schema(implementation = AstronautDTO.class))))
    })
    public ResponseEntity<List<MissionDTO>> getAllMissionsByDestinationId(@PathVariable("id") int destinationId) {
        return ResponseEntity.ok(missionService.getMissionsByDestinationId(destinationId));
    }

    @PostMapping
    @Operation(summary = "Create a new mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mission created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)))
    })
    public ResponseEntity<MissionDTO> addMission(@Valid @RequestBody MissionDTO missionDTO) {
        MissionDTO createdMission = missionService.addMission(missionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMission);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update mission partially", description = "Updates specific fields of an existing mission"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AstronautDTO.class)))
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
            @ApiResponse(responseCode = "204", description = "Mission deleted successfully")})
    public ResponseEntity<Void> deleteMission(@Parameter(description = "ID of mission to delete", required = true, example = "1")
                                              @PathVariable @Min(1) int id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{missionId}/start")
    @Operation(summary = "Start mission",description = "Initiates a mission with the assigned crew, generates mission report, and updates user budget"
    )@ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mission started successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MissionStartResponseDTO.class)
                    )
            )
    })
    public ResponseEntity<?> startMission(
            @Parameter(description = "ID of mission", required = true, example = "1")
            @Valid @PathVariable Integer missionId,
            @RequestBody MissionDTO missionDTO,
            @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        missionParticipantsService.clearMissionCrew(missionId);

        missionDTO.getParticipants().forEach(participant -> {
            missionParticipantsService.addParticipantsToMission(missionId, participant.getAstronautId());
        });

        List<MissionParticipantsDTO> missionParticipantsDTO = missionParticipantsService.showMissionCrew(missionId);

        MissionResultDTO missionResultDTO = missionService.startMission(missionId, missionDTO.getParticipants());

        MissionReportDTO missionReport = missionReportService.createReport(missionId, missionResultDTO, missionParticipantsDTO);

        BudgetDTO updatedBudget = budgetService.updateBudget(
                currentUser.getId(),
                missionId,
                missionResultDTO,
                missionParticipantsDTO
        );
        MissionStartResponseDTO response = new MissionStartResponseDTO(missionReport, updatedBudget);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{missionId}/specializations")
    @Operation(summary = "Add or update  mission specializations",
            description = "Use quantity > 0 to add/update specializations")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Specializations added/updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)
                    )
            )
    })
    public ResponseEntity<MissionDTO> updateSpecializations(
            @Parameter(description = "ID of mission", required = true, example = "1")
            @PathVariable @Min(1) int missionId,
            @Valid @RequestBody List<MissionSpecializationDTO.AddSpecializationRequest> specializations) {
        MissionDTO updatedMission = missionService.addOrUpdateSpecializations(missionId, specializations);
        return ResponseEntity.ok(updatedMission);
    }

    @DeleteMapping("/{missionId}/delete-specialization")
    @Operation( summary = "Delete specialization from mission",
            description = "Removes a specific specialization requirement from a mission"
    )
    @ApiResponses(value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Specialization deleted successfully"
                    )
    })
    public ResponseEntity<Void> deleteSpecialization(
            @Parameter(description = "ID of mission", required = true, example = "1")
            @PathVariable @Min(1) int missionId, @Valid @RequestBody MissionSpecializationDTO.AddSpecializationRequest request) {
        missionService.removeSpecialization(missionId, MissionSpecialization.Specialization.valueOf(request.getSpecialization()));
        return ResponseEntity.noContent().build();
    }
}