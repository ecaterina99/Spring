package com.server.controllers;

import com.server.dto.BudgetDTO;
import com.server.dto.DestinationDTO;
import com.server.services.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@Tag(name = "Budget", description = "Operations related to budget")

public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve payment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Budget not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<BudgetDTO> getBudget(@PathVariable @Min(1) int id) {
        BudgetDTO payment = budgetService.getBudgetById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping()
    @Operation(summary = "Retrieve all budgets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all budgets",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BudgetDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<List<BudgetDTO>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

}
