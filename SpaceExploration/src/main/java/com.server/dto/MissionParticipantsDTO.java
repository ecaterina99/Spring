package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Integer id;
    private Integer missionId;
    private Integer astronautId;
    private String astronautName;
    private String missionName;

    public static MissionParticipantsDTO missionParticipantsDetails(MissionParticipants missionParticipants) {
        return MissionParticipantsDTO.builder()
                .astronautId(missionParticipants.getAstronaut().getId())
                .astronautName(missionParticipants.getAstronaut().getFullName())
                .missionId(missionParticipants.getMission().getId())
                .missionName(missionParticipants.getMission().getMissionName())
                .build();
    }
}
