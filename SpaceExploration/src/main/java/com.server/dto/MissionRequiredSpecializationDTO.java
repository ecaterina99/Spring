package com.server.dto;

import com.server.models.MissionRequiredSpecializations;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Mission required specializations for the crew")
public class MissionRequiredSpecializationDTO {
    private int id;
    private int missionId;
    private MissionRequiredSpecializations.Specialization specialization;
    private int quantityRequired;

    @Schema(description = "Display name of the specialization", example = "Pilot")
    public String getDisplayName() {
        return specialization != null ? specialization.getDisplayName() : "";

    }
        public static MissionRequiredSpecializationDTO create(
                MissionRequiredSpecializations.Specialization specialization,
        int quantity) {
            return MissionRequiredSpecializationDTO.builder()
                    .specialization(specialization)
                    .quantityRequired(quantity)
                    .build();
        }
    public static MissionRequiredSpecializationDTO fromEntity(MissionRequiredSpecializations entity) {
        return MissionRequiredSpecializationDTO.builder()
                .id(entity.getId())
                .missionId(entity.getMission().getId())
                .specialization(entity.getSpecialization())
                .quantityRequired(entity.getQuantityRequired())
                .build();
    }
    }


