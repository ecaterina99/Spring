package com.server.controllers;

import com.server.dto.AstronautDTO;
import com.server.services.AstronautService;
import com.server.util.GlobalApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/astronauts")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Astronauts", description = "Operations related to astronauts")
public class AstronautController implements GlobalApiResponses {

    private final AstronautService astronautService;

    public AstronautController(AstronautService astronautService) {
        this.astronautService = astronautService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve astronaut by ID")
    @ApiResponse(responseCode = "200", description = "Astronaut found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AstronautDTO.class)))
    public ResponseEntity<AstronautDTO> getAstronaut(
            @Parameter(description = "ID of astronaut to retrieve", example = "1")
            @PathVariable int id) {
        return ResponseEntity.ok(astronautService.getAstronautById(id));
    }

    @GetMapping
    @Operation(summary = "Retrieve all astronauts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all astronauts",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AstronautDTO.class))))
    public ResponseEntity<List<AstronautDTO>> getAllAstronauts() {
        return ResponseEntity.ok(astronautService.getAllAstronauts());
    }

    @PostMapping
    @Operation(summary = "Create new astronaut")
    @ApiResponse(responseCode = "201", description = "Astronaut created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AstronautDTO.class)))
    public ResponseEntity<AstronautDTO> addAstronaut(@Valid @RequestBody AstronautDTO astronautDTO) {
        AstronautDTO created = astronautService.addAstronaut(astronautDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update astronaut")
    @ApiResponse(responseCode = "200", description = "Astronaut updated successfully",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AstronautDTO.class)))
    public ResponseEntity<AstronautDTO> updateAstronaut(
            @PathVariable int id,
            @Valid @RequestBody AstronautDTO.AstronautUpdateDTO astronautDTO) {
        AstronautDTO updated = astronautService.updateAstronaut(id, astronautDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete astronaut")
    @ApiResponse(responseCode = "204", description = "Astronaut deleted successfully")
    public ResponseEntity<Void> deleteAstronaut(@PathVariable int id) {
        astronautService.deleteAstronaut(id);
        return ResponseEntity.noContent().build();
    }
}