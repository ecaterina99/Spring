package com.server.dto;
import com.server.models.Mission;
import com.server.models.MissionReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
        return new MissionReportDTO(missionReport.getId(),
                missionReport.getResultsDescription(),
                missionReport.isSuccessful(),
                missionReport.getMission().getId(),
                missionReport.getMission().getMissionName(),
                missionReport.getMission().getDestination().getDestinationName(),
                missionReport.getMission().getDifficultyLevel(),

                missionReport.getMission().getMissionParticipants().
                        stream()
                        .map(mp -> MissionParticipantsDTO.astronautDetails(mp)).toList(),
                                missionReport.getMission().getPayment().getProceeds());

    }
}