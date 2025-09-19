package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Mission required specializations for the crew")
public class MissionRequiredSpecializationDTO {
    private Integer id;
    private Integer missionId;
    private MissionRequiredSpecializations.Specialization specialization;
    private Integer quantityRequired;

        public static MissionRequiredSpecializationDTO create(
                MissionRequiredSpecializations.Specialization specialization,
        int quantity) {
            return MissionRequiredSpecializationDTO.builder()
                    .specialization(specialization)
                    .quantityRequired(quantity)
                    .build();
        }
    }


