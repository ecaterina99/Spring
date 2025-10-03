package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Mission name is required")
    @Size(min = 2, max = 100, message = "Mission name must be between 2 and 100 characters")
    private String name;
    @NotBlank(message = "Missin code is required")
    @Size(min = 4, max = 6, message = "Mission name must be between 2 and 6 characters")
    private String code;
    @NotBlank(message = "Mission description is required")
    private String description;
    @Min(value = 1)
    @NotNull
    private Integer durationDays;
    @Min(value = 1)
    @NotNull
    private Integer crewSize;
    @Min(value = 1)
    @NotNull
    private Integer scoreValue;
    @NotBlank(message = "Potential issues description is required")
    private String potentialIssues;
    private String imgUrl;
    @NotNull(message = "Difficulty level is required")
    private Mission.DifficultyLevel difficultyLevel;
    private Integer destinationId;
    private String destinationName;
    @Min(value = 1)
    @NotNull
    private Integer paymentAmount;
    private List<MissionSpecializationDTO> specializations = new ArrayList<>();

    public static MissionDTO missionWithDetails(Mission mission) {
        return MissionDTO.builder()
                .id(mission.getId())
                .name(mission.getName())
                .code(mission.getCode())
                .description(mission.getDescription())
                .crewSize(mission.getCrewSize())
                .difficultyLevel(mission.getDifficultyLevel())
                .potentialIssues(mission.getPotentialIssues())
                .durationDays(mission.getDurationDays())
                .paymentAmount(mission.getPaymentAmount())
                .destinationId(mission.getDestination().getId())
                .destinationName(mission.getDestination().getDestinationName())
                .scoreValue(mission.getScoreValue())
                .imgUrl(mission.getImgUrl())
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Mission update information - all fields are optional for partial updates")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class MissionUpdateDTO {

        @Size(min = 2, max = 100, message = "Mission name must be between 2 and 100 characters")
        @Schema(description = "Mission name", example = "Mars Exploration Alpha")
        private String name;

        @Size(min = 4, max = 6, message = "Mission code must be between 4 and 6 characters")
        @Schema(description = "Unique mission code", example = "MRS001")
        private String code;

        @Schema(description = "Mission description")
        private String description;

        @Min(value = 1, message = "Duration must be at least 1 day")
        @Schema(description = "Mission duration in days", example = "30")
        private Integer durationDays;

        @Min(value = 1, message = "Crew size must be at least 1")
        @Schema(description = "Required crew size", example = "5")
        private Integer crewSize;

        @Min(value = 1, message = "Score value must be at least 1")
        @Schema(description = "Mission score value", example = "1000")
        private Integer scoreValue;

        @Schema(description = "Potential issues description")
        private String potentialIssues;

        @Schema(description = "Mission image URL")
        private String imageUrl;

        @Schema(description = "Mission difficulty level")
        private Mission.DifficultyLevel difficultyLevel;

        @Min(value = 1, message = "Destination ID must be positive")
        @Schema(description = "Destination ID", example = "1")
        private Integer destinationId;

        @Min(value = 1, message = "Payment amount must be at least 1")
        @Schema(description = "Payment amount", example = "50000")
        private Integer paymentAmount;
    }

}

