package com.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Budget details")
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    private int id;
    private int initialBudget;
    private int finalBudget;
    private int missionId;
}
