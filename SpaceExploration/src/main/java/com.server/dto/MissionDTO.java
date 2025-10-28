package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information about mission")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class MissionDTO {
    @Schema(description = "Mission ID", example = "1")
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 100, message = "Mission name must be between 2 and 100 characters")
    @Schema(description = "Mission name", example = "Operation Homeland")
    private String name;

    @NotBlank
    @Size(min = 4, max = 6, message = "Mission name must be between 2 and 6 characters")
    @Schema(description = "Mission code", example = "ER-1")
    private String code;

    @NotBlank
    @Schema(description = "Mission description", example = "Operation Homeland is a global initiative focused on...")
    private String description;

    @Min(value = 1)
    @NotNull
    @Schema(description = "Duration days", example = "100")
    private Integer durationDays;

    @Min(value = 1)
    @NotNull
    @Schema(description = "Crew size", example = "4")
    private Integer crewSize;

    @Min(value = 1)
    @NotNull
    @Schema(description = "Score Value", example = "1000")
    private Integer scoreValue;

    @NotBlank
    @Schema(description = "Potential issues", example = "Dust storms may disrupt communications for up to 3 weeks...")
    private String potentialIssues;

    @Schema(description = "Image path", example = "m1.jpg")
    private String imageUrl;

    @Schema(description = "Difficulty level", example = "HIGH")
    @NotNull
    private Mission.DifficultyLevel difficultyLevel;

    @Schema(description = "Destination ID", example = "1")
    private Integer destinationId;

    @Min(value = 1)
    @NotNull
    @Schema(description = "Payment Amount:", example = "250000")
    private Integer paymentAmount;

    private String destinationName;
    private List<MissionSpecializationDTO> specializations = new ArrayList<>();
    private List<MissionParticipantsDTO> participants = new ArrayList<>();

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
                .imageUrl(mission.getImageUrl())
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

        @Schema(description = "Mission description", example = "Operation Homeland is a global initiative focused on...")
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

        @Schema(description = "Potential issues", example = "Dust storms may disrupt communications for up to 3 weeks...")
        private String potentialIssues;

        @Schema(description = "Image path", example = "m1.jpg")
        private String imageUrl;

        @Schema(description = "Difficulty level", example = "HIGH")
        private Mission.DifficultyLevel difficultyLevel;

        @Min(value = 1, message = "Destination ID must be positive")
        @Schema(description = "Destination ID", example = "1")
        private Integer destinationId;

        @Min(value = 1, message = "Payment amount must be at least 1")
        @Schema(description = "Payment amount", example = "50000")
        private Integer paymentAmount;
    }
}

