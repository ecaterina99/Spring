package com.server.util;

import com.server.dto.BudgetDTO;
import com.server.dto.MissionReportDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissionStartResponse {
    private MissionReportDTO missionReport;
    private BudgetDTO updatedBudget;
}