package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
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

    private List<MissionSpecializationDTO> specializations = new ArrayList<>();

    private List<MissionParticipantsDTO> participants = new ArrayList<>();

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
            private MissionSpecialization.Specialization specialization;
            private int quantity;

            public SpecializationSummaryDTO(MissionSpecialization.Specialization specialization, int quantity) {
                this.specialization = specialization;
                this.quantity = quantity;
            }
        }

    }

    @Data
    @NoArgsConstructor
    public static class MissionWithoutDetailsDTO {
        private String missionName;
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
                .specializations(
                        mission.getMissionSpecializations().stream()
                                .map(mrs -> MissionSpecializationDTO.builder()
                                        .specialization(mrs.getSpecialization())
                                        .quantity(mrs.getQuantity())
                                        .build()
                                )
                                .toList()
                )

                .participants(
                        mission.getMissionParticipants().stream()
                                .map(mp -> MissionParticipantsDTO.builder()
                                        .astronautId(mp.getAstronaut().getId())
                                        .astronautName(mp.getAstronaut().getFirstName())
                                        .build()
                                )
                                .toList()
                )

                .build();
    }


}

