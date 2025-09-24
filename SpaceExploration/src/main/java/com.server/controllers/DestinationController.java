package com.server.controllers;

import com.server.dto.DestinationDTO;
import com.server.services.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@Tag(name = "Destinations", description = "Operations related to travel destinations")

public class DestinationController {
    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve destination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DestinationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Destination not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<DestinationDTO> getDestination(@Parameter(description = "ID of destination to retrieve", required = true, example = "1")
                                                         @PathVariable @Min(1) int id) {
        DestinationDTO destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    @GetMapping()
    @Operation(summary = "Get all destinations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all destinations",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DestinationDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<DestinationDTO>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }

    @GetMapping("/withMissions/{id}")
    @Operation(summary = "Get destination with all missions by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination found", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DestinationDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Destination not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<DestinationDTO.DestinationWithMissionsDTO> getDestinationAndMissions(@PathVariable int id) {
        DestinationDTO.DestinationWithMissionsDTO destination = destinationService.getDestinationByIdWithMission(id);
        return ResponseEntity.ok(destination);
    }

}