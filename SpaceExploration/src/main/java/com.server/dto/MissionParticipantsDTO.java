package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Astronaut;
import com.server.models.MissionParticipants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Mission participants information")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MissionParticipantsDTO {
    private String missionName;
    private String astronautName;
    private Integer id;
    private Integer missionId;
    private Integer astronautId;
    private Astronaut.Specialization specialization;
    private Integer overallScore;

    public static MissionParticipantsDTO missionParticipantsDetails(MissionParticipants missionParticipants) {
        return MissionParticipantsDTO.builder()
                .missionName(missionParticipants.getMission().getMissionName())
                .astronautName(missionParticipants.getAstronaut().getFirstName())
                .specialization(missionParticipants.getAstronaut().getSpecialization())
                .overallScore(missionParticipants.getAstronaut().getOverallScore())
                .build();
    }
}
