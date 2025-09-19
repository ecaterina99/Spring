package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.MissionRequiredSpecializations;
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
@Schema(description = "Mission required specializations for the crew")
public class MissionRequiredSpecializationDTO {
    private Integer id;
    private Integer missionId;
    private MissionRequiredSpecializations.Specialization specialization;
    private Integer quantityRequired;

        public static MissionRequiredSpecializationDTO create(
                MissionRequiredSpecializations missionRequiredSpecializations) {
            return MissionRequiredSpecializationDTO.builder()
                    .missionId(missionRequiredSpecializations.getMission().getId())
                    .specialization(missionRequiredSpecializations.getSpecialization())
                    .quantityRequired(missionRequiredSpecializations.getQuantityRequired()
                    )
                    .build();
        }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Request to add specialization to mission")
    public static class AddSpecializationRequest {
        @NotNull(message = "Specialization is required")
        @Schema(description = "Specialization type", example = "PILOT", allowableValues = {"PILOT", "DOCTOR", "SCIENTIST"})
        private String specialization;

        @Min(value = 1, message = "Quantity must be at least 1")
        @Schema(description = "Quantity required", example = "2")
        private int quantity = 1;
    }

}


