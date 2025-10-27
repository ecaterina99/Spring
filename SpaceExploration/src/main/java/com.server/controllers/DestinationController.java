package com.server.controllers;

import com.server.dto.DestinationDTO;
import com.server.services.DestinationService;
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
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@Tag(name = "Destinations", description = "Operations related to travel destinations")
@SecurityRequirement(name = "Bearer Authentication")

public class DestinationController implements GlobalApiResponses {
    private final DestinationService destinationService;
    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve destination by ID")
    @ApiResponse(responseCode = "200", description = "Destination found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DestinationDTO.class)))
    public ResponseEntity<DestinationDTO> getDestination(@Parameter(description = "ID of destination to retrieve", required = true, example = "1")
                                                         @PathVariable @Min(1) int id) {
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }

    @GetMapping()
    @Operation(summary = "Get all destinations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all destinations",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DestinationDTO.class))))
    })
    public ResponseEntity<List<DestinationDTO>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }

    @GetMapping("/missions/{id}")
    @Operation(summary = "Get destination with all missions by destination ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination found", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DestinationDTO.class))))
    })
    public ResponseEntity<DestinationDTO.DestinationWithMissionsDTO> getDestinationAndMissions(@PathVariable int id) {
        return ResponseEntity.ok(destinationService.getDestinationByIdWithMission(id));
    }

}