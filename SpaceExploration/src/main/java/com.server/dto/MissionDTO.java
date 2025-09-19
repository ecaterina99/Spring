package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Mission information")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class MissionDTO {
    private int id;
    private String missionName;
    private String code;
    private String description;
    private Integer durationDays;
    private Integer crewSizeRequired;
    private Integer scoreValue;
    private String potentialIssues;
    private String image;
    private Mission.DifficultyLevel difficultyLevel;
    private Integer destinationId;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MissionSummaryDTO {
        private int id;
        private String missionName;
        private Mission.DifficultyLevel difficultyLevel;
        private String destinationName;
        private String resultsDescription;
        private boolean isSuccessful;
        private List<SpecializationSummaryDTO> specializations = new ArrayList<>();

        @Data
        @NoArgsConstructor
        public static class SpecializationSummaryDTO {
            private MissionRequiredSpecializations.Specialization specialization;
            private int quantityRequired;

            public SpecializationSummaryDTO(MissionRequiredSpecializations.Specialization specialization, int quantityRequired) {
                this.specialization = specialization;
                this.quantityRequired = quantityRequired;
            }
        }

    }

    @Data
    @NoArgsConstructor
    public static class MissionWithoutDetailsDTO {
        private String missionName;

       /* public MissionWithoutDetailsDTO(String missionName) {
            this.missionName = missionName;
        }

        */
    }

    public static MissionDTO withDetails(Mission mission) {
        return MissionDTO.builder()
                .id(mission.getId())
                .missionName(mission.getMissionName())
                .code(mission.getCode())
                .description(mission.getDescription())
                .crewSizeRequired(mission.getCrewSizeRequired())
                .difficultyLevel(mission.getDifficultyLevel())
                .potentialIssues(mission.getPotentialIssues())
                .durationDays(mission.getDurationDays())
                .destinationId(mission.getDestination().getId())
                .destinationName(mission.getDestination().getDestinationName())
                .scoreValue(mission.getScoreValue())
                .requiredSpecializations(
                        mission.getMissionRequiredSpecializations().stream()
                                .map(mrs -> MissionRequiredSpecializationDTO.builder()
                                        .specialization(mrs.getSpecialization())
                                        .quantityRequired(mrs.getQuantityRequired())
                                        .build()
                                )
                                .toList()
                )

                .participants(
                        mission.getMissionParticipants().stream()
                                .map(mp -> MissionParticipantsDTO.builder()
                                        .astronautId(mp.getAstronaut().getId())
                                        .astronautName(mp.getAstronaut().getFullName())
                                        .build()
                                )
                                .toList()
                )

                .build();
    }


}

