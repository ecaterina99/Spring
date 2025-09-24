package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Mission;
import com.server.models.MissionReport;
import com.server.models.MissionSpecialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@Schema(description = "Mission report")
public class MissionReportDTO {
    private int id;
    private boolean isSuccessful;
    @NotBlank(message = "Results description is required")
    @Size(min = 2, message = "Results description must be longer")
    private String resultsDescription;
    @NotNull
    private int missionId;
    @NotBlank
    private String missionName;
    @NotBlank
    private String destinationName;
    @NotBlank
    private Mission.DifficultyLevel difficultyLevel;
    private List<MissionParticipantsDTO> participants;
    @NotNull
    private Integer paymentAmount;

    public static MissionReportDTO fromMissionReport(MissionReport missionReport) {
        return MissionReportDTO.builder()
                .id(missionReport.getId())
                .isSuccessful(missionReport.isSuccessful())
                .resultsDescription(missionReport.getResultsDescription())
                .missionId(missionReport.getMission().getId())
                .missionName(missionReport.getMission().getName())
                .destinationName(missionReport.getMission().getDestination().getDestinationName())
                .paymentAmount(missionReport.getMission().getPaymentAmount())
                .build();
    }

}