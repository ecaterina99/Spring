package com.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterCriteriaDTO {
    private String difficultyLevel;
    private Integer destinationId;

    public boolean isActive() {
        return (difficultyLevel != null && !difficultyLevel.isEmpty())
                || (destinationId != null && destinationId > 0);
    }

    public String getFilterDescription() {
        List<String> parts = new ArrayList<>();
        if (difficultyLevel != null && !difficultyLevel.isEmpty()) {
            parts.add("Difficulty: " + difficultyLevel);
        }
        if (destinationId != null && destinationId > 0) {
            parts.add("Destination ID: " + destinationId);
        }
        return String.join(", ", parts);
    }
}