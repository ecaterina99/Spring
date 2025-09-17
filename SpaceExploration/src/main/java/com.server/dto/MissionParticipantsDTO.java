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
    private int astronautId;
    private String astronautName;


    public static MissionParticipantsDTO astronautDetails(MissionParticipants missionParticipants) {
        return MissionParticipantsDTO.builder()
                .astronautId(missionParticipants.getAstronaut().getId())
                .astronautName(missionParticipants.getAstronaut().getFullName())
                .build();
    }
}
