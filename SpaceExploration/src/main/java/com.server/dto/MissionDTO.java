package com.server.dto;

import com.server.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Mission information")
public class MissionDTO {
    private int id;
    private String missionName;
    private String code;
    private String description;
    private int durationDays;
    private int crewSizeRequired;
    private String requiredSpecializations;
    private int scoreValue;
    private String potentialIssues;
    private String image;
    private Mission.DifficultyLevel difficultyLevel;
    private int destinationId;
    private List<MissionParticipants> missionParticipants;
    private String destinationName;


    @Data
    public static class MissionSummaryDTO {
        private int id;
        private String missionName;
        private Mission.DifficultyLevel difficultyLevel;
        private String destinationName;
        private String resultsDescription;
        private boolean isSuccessful;

        public MissionSummaryDTO(
                int id,
                @NotBlank(message = "Mission name is required")
                @Size(min = 2, max = 100, message = "Mission name must be between 2 and 100 characters")
                String missionName,
                Mission.DifficultyLevel difficultyLevel,
                String destinationName,
                boolean isSuccessful,
                String resultsDescription) {
            this.id = id;
            this.missionName = missionName;
            this.difficultyLevel = difficultyLevel;
            this.destinationName = destinationName;
            this.isSuccessful = isSuccessful;
            this.resultsDescription = resultsDescription;
        }
    }
}
