package com.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissionStartResponseDTO {
    private MissionReportDTO missionReport;
    private BudgetDTO updatedBudget;
}