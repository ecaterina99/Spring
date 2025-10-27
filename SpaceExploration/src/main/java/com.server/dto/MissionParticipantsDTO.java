package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Astronaut;
import com.server.models.MissionParticipants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Information about mission participants")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MissionParticipantsDTO {
    private Integer id;
    @NotNull
    @Schema(description = "Mission ID", example = "1")
    private Integer missionId;
    @Schema(description = "Astronaut ID", example = "1")
    @NotNull
    private Integer astronautId;
    private Astronaut.Specialization specialization;
    private Integer overallScore;
    private Integer crewSize;
    private Astronaut.HealthStatus healthStatus;
    private Integer dailyRate;
    private String missionName;
    private String astronautName;

    public static MissionParticipantsDTO missionParticipantsDetails(MissionParticipants missionParticipants) {
        return MissionParticipantsDTO.builder()
                .missionName(missionParticipants.getMission().getName())
                .astronautName(missionParticipants.getAstronaut().getFirstName()+" "+missionParticipants.getAstronaut().getLastName())
                .specialization(missionParticipants.getAstronaut().getSpecialization())
                .overallScore(missionParticipants.getAstronaut().getOverallScore())
                .dailyRate(missionParticipants.getAstronaut().getDailyRate())
                .build();
    }
}
