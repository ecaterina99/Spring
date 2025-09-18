package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Mission information")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MissionDTO {
    private int id;
    private String missionName;
    private String code;
    private String description;
    private int durationDays;
    private int crewSizeRequired;
    private int scoreValue;
    private String potentialIssues;
    private String image;
    private Mission.DifficultyLevel difficultyLevel;
    private int destinationId;
    private List<MissionParticipants> missionParticipants;
    private String destinationName;

    private List<MissionRequiredSpecializationDTO> requiredSpecializations = new ArrayList<>();

    private List<MissionParticipantsDTO> participants = new ArrayList<>();

    public void addRequiredSpecialization(MissionRequiredSpecializations.Specialization specialization, int quantity) {
        MissionRequiredSpecializationDTO dto = new MissionRequiredSpecializationDTO();
        dto.setSpecialization(specialization);
        dto.setQuantityRequired(quantity);
        this.requiredSpecializations.add(dto);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MissionSummaryDTO {
        private int id;
        private String missionName;
        private Mission.DifficultyLevel difficultyLevel;
        private String destinationName;
        private String resultsDescription;
        private boolean isSuccessful;
    //    private List<MissionRequiredSpecializations.Specialization> specializations;
        private List<SpecializationSummaryDTO> specializations = new ArrayList<>();

       /* public MissionSummaryDTO(
                int id,
                @NotBlank(message = "Mission name is required")
                @Size(min = 2, max = 100, message = "Mission name must be between 2 and 100 characters")
                String missionName,
                Mission.DifficultyLevel difficultyLevel,
                String destinationName,
                boolean isSuccessful,
                String resultsDescription,
            List<MissionRequiredSpecializations.Specialization> specializations)
        {
            this.id = id;
            this.missionName = missionName;
            this.difficultyLevel = difficultyLevel;
            this.destinationName = destinationName;
            this.isSuccessful = isSuccessful;
            this.resultsDescription = resultsDescription;
           this.specializations = specializations;

        }

        */
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       public static class SpecializationSummaryDTO {
           private MissionRequiredSpecializations.Specialization specialization;
           private int quantityRequired;
           private String displayName;

           public SpecializationSummaryDTO(MissionRequiredSpecializations.Specialization specialization, int quantityRequired) {
               this.specialization = specialization;
               this.quantityRequired = quantityRequired;
               this.displayName = specialization.getDisplayName();
           }
           }
    }
}
