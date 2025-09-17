package com.server.controllers;

import com.server.dto.MissionParticipantsDTO;
import com.server.services.MissionParticipantsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all mission participants"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<MissionParticipantsDTO>> getAllParticipantsByMissionId(
            @PathVariable @Min(1) int missionId) {
        List<MissionParticipantsDTO> participants = missionParticipantsService.getAllParticipantsByMissionId(missionId);
        return ResponseEntity.ok(participants);
    }
}
