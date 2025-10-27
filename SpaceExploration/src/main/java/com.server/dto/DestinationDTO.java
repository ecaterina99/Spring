package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Information about space destination")
public class DestinationDTO {
    @Schema(description = "Destination ID", example = "1")
    private int id;

    @NotBlank
    @Size(min = 2, max = 100, message = "Destination name must be between 2 and 100 characters")
    @Schema(description = "Destination name", example = "Mars")
    private String destinationName;

    @NotBlank
    @Size(min = 2, max = 50, message = "Distance from Earth must be between 2 and 50 characters")
    @Schema(description = "Distance from Earth", example = "225 million km")
    private String distanceFromEarth;

    @NotBlank
    @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters")
    @Schema(description = "Description", example = "Mars is the fourth planet from the Sun.....")
    private String description;

    @Schema(description = "Gravity", example = "10")
    private Integer gravity;

    @Schema(description = "Image path", example = "/images/1.jpg")
    private String image;

    @Schema(description = "Entity type", example = "PLANET")
    @NotNull(message = "Entity type is required")
    private Destination.EntityType entityType;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "Space destination with missions information")
    public static class DestinationWithMissionsDTO {
        @Schema(description = "Destination ID", example = "1")
        private int id;
        @Schema(description = "Destination name", example = "Mars")
        private String destinationName;
        @Schema(description = "Distance from Earth", example = "225 million km")
        private String distanceFromEarth;
        @Schema(description = "Description", example = "Mars is the fourth planet from the Sun.....")
        private String description;
        @Schema(description = "Gravity", example = "10")
        private Integer gravity;
        @Schema(description = "Image path", example = "/images/1.jpg")
        private String image;
        @Schema(description = "Entity type", example = "PLANET")
        private Destination.EntityType entityType;
        @Schema(description = "List of missions for this destination")
        private List<MissionDTO> missions;


        public static DestinationWithMissionsDTO DestinationWithMissions(Destination destination) {
            return DestinationWithMissionsDTO.builder()
                    .id(destination.getId())
                    .destinationName(destination.getDestinationName())
                    .distanceFromEarth(destination.getDistanceFromEarth())
                    .description(destination.getDescription())
                    .gravity(destination.getGravity())
                    .image(destination.getImage())
                    .entityType(destination.getEntityType())
                    .missions(destination.getMissions().stream()
                            .map(mission -> MissionDTO.builder()
                                    .id(mission.getId())
                                    .name(mission.getName())
                                    .crewSize(mission.getCrewSize())
                                    .description(mission.getDescription())
                                    .imgUrl(mission.getImgUrl())
                                    .potentialIssues(mission.getPotentialIssues())
                                    .code(mission.getCode())
                                    .difficultyLevel(mission.getDifficultyLevel())
                                    .paymentAmount(mission.getPaymentAmount())
                                    .scoreValue(mission.getScoreValue())
                                    .durationDays(mission.getDurationDays())
                                    .specializations(
                                            mission.getMissionSpecializations().stream()
                                                    .map(mrs -> MissionSpecializationDTO.builder()
                                                            .specialization(mrs.getSpecialization())
                                                            .quantity(mrs.getQuantity())
                                                            .build()
                                                    )
                                                    .toList()
                                    )
                                    .build())
                            .toList())
                    .build();
        }
    }
}


