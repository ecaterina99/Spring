package com.server.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Mission;
import com.server.models.MissionReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)

@Builder
@Schema(description = "Mission report details")
public class MissionReportDTO {
    private int id;
    private String resultsDescription;
    private boolean isSuccessful;
    private int missionId;
    private String missionName;
    private String destinationName;
    private Mission.DifficultyLevel difficultyLevel;
    private List<MissionParticipantsDTO> participants;
    private Integer proceeds;

    public static MissionReportDTO withDetails(MissionReport missionReport) {
        return  MissionReportDTO.builder()
                .id(missionReport.getId())
                .resultsDescription(missionReport.getResultsDescription())
                .isSuccessful(missionReport.isSuccessful())
                        .missionId(missionReport.getMission().getId())
                                .missionName(missionReport.getMission().getMissionName())
                                        .destinationName(missionReport.getMission().getDestination().getDestinationName())
                                                .difficultyLevel(missionReport.getMission().getDifficultyLevel())
                .participants(
                        missionReport.getMission().getMissionParticipants().stream()
                                .map(mp -> MissionParticipantsDTO.builder()
                                        .astronautId(mp.getAstronaut().getId())
                                        .astronautName(mp.getAstronaut().getFullName())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
    }
