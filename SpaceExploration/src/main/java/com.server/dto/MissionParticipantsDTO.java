package com.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Mission participants information")
public class MissionParticipantsDTO {
    private int id;
    private int missionId;
    private int astronautId;
}
