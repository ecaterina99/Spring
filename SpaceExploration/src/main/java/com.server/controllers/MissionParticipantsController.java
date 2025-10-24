package com.server.controllers;

import com.server.dto.MissionParticipantsDTO;
import com.server.services.MissionParticipantsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mission-participants")

@Tag(name = "Mission participants", description = "Missions participants details")
public class MissionParticipantsController {

    private final MissionParticipantsService missionParticipantsService;

    public MissionParticipantsController(MissionParticipantsService missionParticipantsService) {
        this.missionParticipantsService = missionParticipantsService;
    }

    @GetMapping("/{missionId}")
    @Operation(summary = "Get all participants for a mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all mission participants",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionParticipantsDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<MissionParticipantsDTO>> getAllParticipantsByMissionId(
            @PathVariable @Min(1) int missionId) {

        List<MissionParticipantsDTO> participants = missionParticipantsService.showMissionCrew(missionId);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/add/{missionId}/{astronautId}")
    @Operation(summary = "Add participant to mission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Astronaut added to the mission successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionParticipantsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<MissionParticipantsDTO> addAstronautToMission(@PathVariable int missionId, @PathVariable int astronautId) {
        MissionParticipantsDTO addedParticipant = missionParticipantsService.addParticipantsToMission(missionId, astronautId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedParticipant);
    }
}
