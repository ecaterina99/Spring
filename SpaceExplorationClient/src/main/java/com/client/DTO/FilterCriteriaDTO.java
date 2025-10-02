package com.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}