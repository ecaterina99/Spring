package com.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Budget details")
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    private int id;
    @NotNull(message = "Initial budget is required")
    private Integer initialBudget;
    @NotNull(message = "Final budget is required")
    private Integer finalBudget;
    @NotNull(message = "Mission is is required")
    private Integer missionId;
}
