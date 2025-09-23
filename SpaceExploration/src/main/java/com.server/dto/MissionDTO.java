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
    private int  id;
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
    private String destinationName;
    private Integer  paymentAmount;
    private List<MissionSpecializationDTO> specializations = new ArrayList<>();

    public static MissionDTO missionWithDetails(Mission mission) {
        return MissionDTO.builder()
                .id(mission.getId())
                .missionName(mission.getMissionName())
                .code(mission.getCode())
                .description(mission.getDescription())
                .crewSizeRequired(mission.getCrewSizeRequired())
                .difficultyLevel(mission.getDifficultyLevel())
                .potentialIssues(mission.getPotentialIssues())
                .durationDays(mission.getDurationDays())
                .paymentAmount(mission.getPaymentAmount())
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
                .build();
    }

}

