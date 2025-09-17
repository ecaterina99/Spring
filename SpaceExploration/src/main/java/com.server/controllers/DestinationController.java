package com.server.controllers;

import com.server.dto.DestinationDTO;
import com.server.services.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@Tag(name = "Destinations", description = "Operations related to travel destinations")

public class DestinationController {
    private  final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get destination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination found"),
            @ApiResponse(responseCode = "404", description = "Destination not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<DestinationDTO> getDestination(@PathVariable int id) {
        DestinationDTO destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    @GetMapping()
    @Operation(summary = "Get all destinations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all destinations"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<DestinationDTO>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }

}