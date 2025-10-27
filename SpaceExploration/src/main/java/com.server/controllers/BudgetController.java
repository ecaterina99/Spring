package com.server.controllers;

import com.server.dto.BudgetDTO;
import com.server.dto.DestinationDTO;
import com.server.models.User;
import com.server.repositories.UserRepository;
import com.server.services.BudgetService;
import com.server.util.GlobalApiResponses;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@Tag(name = "Budget", description = "Operations related to budget")
@SecurityRequirement(name = "Bearer Authentication")

public class BudgetController implements GlobalApiResponses {
    private final BudgetService budgetService;
    private final UserRepository userRepository;

    public BudgetController(BudgetService budgetService, UserRepository userRepository) {
        this.budgetService = budgetService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Retrieve budget by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class)))
    })
    public ResponseEntity<BudgetDTO> getBudgetByUserId(
            @PathVariable Integer userId,
            @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        BudgetDTO budget = budgetService.getUserBudget(currentUser.getId());
        return ResponseEntity.ok(budget);
    }
}

