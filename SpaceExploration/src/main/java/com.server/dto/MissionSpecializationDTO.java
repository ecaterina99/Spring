package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.MissionSpecialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Mission specializations required for the crew")
public class MissionSpecializationDTO {
    private Integer id;
    @NotNull
    private Integer missionId;
    @NotBlank(message = "Specialization is required")
    @Schema(description = "Required specialization type",
            allowableValues = {"PILOT", "ENGINEER", "SCIENTIST", "DOCTOR", "GEOLOGIST"})
    private MissionSpecialization.Specialization specialization;
    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10, message = "Quantity cannot exceed 10")
    private Integer quantity;

    public static MissionSpecializationDTO create(
            MissionSpecialization missionSpecialization) {
        return MissionSpecializationDTO.builder()
                .missionId(missionSpecialization.getMission().getId())
                .specialization(missionSpecialization.getSpecialization())
                .quantity(missionSpecialization.getQuantity()
                )
                .build();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddSpecializationRequest {

        @NotBlank(message = "Specialization is required")
        @Schema(description = "Required specialization type",
                allowableValues = {"PILOT", "ENGINEER", "SCIENTIST", "DOCTOR", "GEOLOGIST"},
                example = "PILOT")
        private String specialization;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 10, message = "Quantity cannot exceed 10")
        @Schema(description = "Number of specialists required",
                example = "2",
                minimum = "1",
                maximum = "10")
        private Integer quantity;
    }
}


