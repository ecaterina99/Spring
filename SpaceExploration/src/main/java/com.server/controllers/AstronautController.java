package com.server.controllers;

import com.server.dto.AstronautDTO;
import com.server.services.AstronautService;
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
@RequestMapping("/astronauts")
@Tag(name = "Astronauts", description = "Operations related to astronauts")
public class AstronautController {


    private final AstronautService astronautService;
    public AstronautController(AstronautService astronautService) {
        this.astronautService = astronautService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve astronaut by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Astronaut found"),
            @ApiResponse(responseCode = "404", description = "Astronaut not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    public ResponseEntity<AstronautDTO> getAstronaut(@PathVariable int id) {
        AstronautDTO astronaut = astronautService.getAstronautById(id);
        return ResponseEntity.ok(astronaut);
    }

    @GetMapping()
    @Operation(summary = "Retrieve all astronauts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all astronauts"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<AstronautDTO>> getAllAstronauts() {
        return ResponseEntity.ok(astronautService.getAllAstronauts());
    }

    @PostMapping
    @Operation(summary = "Create new astronaut")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Astronaut created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<AstronautDTO> addAstronaut(@Valid @RequestBody AstronautDTO astronautDTO) {
        AstronautDTO createdAstronaut = astronautService.addAstronaut(astronautDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAstronaut);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update astronaut partially ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Astronaut updated successfully"),
            @ApiResponse(responseCode = "404", description = "Astronaut not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<AstronautDTO> updateAstronaut(
            @PathVariable int id,
            @RequestBody AstronautDTO astronautDTO) {
        AstronautDTO updatedAstronaut = astronautService.updateAstronaut(id,astronautDTO);
        return ResponseEntity.ok(updatedAstronaut);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete astronaut")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Astronaut deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Astronaut not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteAstronaut(@PathVariable int id) {
        astronautService.deleteAstronaut(id);
        return ResponseEntity.noContent().build();
    }
}
