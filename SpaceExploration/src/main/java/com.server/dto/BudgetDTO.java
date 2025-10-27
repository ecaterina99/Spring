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
    @Schema(description = "Budget ID", example = "10")
    private int id;
    @Schema(description = "Initial budget", example = "1000000")
    @NotNull(message = "Initial budget is required")
    private Integer currentBudget  = 1000000;
    @Schema(description = "User ID", example = "1")
    @NotNull(message = "User id is required")
    private Integer userId;
}
