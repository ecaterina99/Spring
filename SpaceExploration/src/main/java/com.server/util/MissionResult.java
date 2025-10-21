package com.server.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionResult {
    private boolean success;
    private int successChance;
    private List<String> issues = new ArrayList<>();
    private boolean alienAttack;
    private int crewSizeDeficit;
    private List<String> missingSpecializations = new ArrayList<>();
    private List<String> notReadyAstronauts = new ArrayList<>();
    private int totalSalary;
}