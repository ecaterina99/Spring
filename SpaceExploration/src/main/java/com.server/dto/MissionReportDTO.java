package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Schema(description = "Report ID", example = "1")
    private int id;

    @Schema(description = "Mission success", example = "1")
    @JsonProperty("isSuccessful")
    @NotNull
    private boolean isSuccessful;

    @Schema(description = "Mission ID", example = "1")
    @NotNull
    private int missionId;

    private String missionName;
    private String destinationName;
    private Mission.DifficultyLevel difficultyLevel;
    private List<MissionParticipantsDTO> participants;
    private Integer paymentAmount;
    private String astronautName;
    private Integer crewSize;
    private String specialization;

    private List<MissionSpecializationDTO> specializations = new ArrayList<>();

    private Integer successChance;
    private List<String> issues = new ArrayList<>();
    private Boolean alienAttack;
    private Integer crewSizeDeficit;
    private List<String> missingSpecializations = new ArrayList<>();
    private List<String> notReadyAstronauts = new ArrayList<>();
    private int totalSalary;


    public static MissionReportDTO fromMissionReport(MissionReport missionReport) {
        return MissionReportDTO.builder()
                .id(missionReport.getId())
                .isSuccessful(missionReport.isSuccessful())
                .missionId(missionReport.getMission().getId())
                .missionName(missionReport.getMission().getName())
                .destinationName(missionReport.getMission().getDestination().getDestinationName())
                .paymentAmount(missionReport.getMission().getPaymentAmount())
                .build();
    }
}