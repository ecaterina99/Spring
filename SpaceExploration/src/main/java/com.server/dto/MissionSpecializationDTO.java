package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.MissionSpecialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Mission specializations required for the crew")
public class MissionSpecializationDTO {
    private Integer id;
    private Integer missionId;
    private MissionSpecialization.Specialization specialization;
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddSpecializationRequest {
        @NotNull(message = "Specialization is required")
        @Schema(description = "Specialization type", example = "PILOT", allowableValues = {"PILOT", "DOCTOR", "SCIENTIST"})
        private String specialization;

        @Min(value = 1, message = "Quantity must be at least 1")
        @Schema(description = "Quantity required", example = "2")
        private int quantity = 1;
    }
}


