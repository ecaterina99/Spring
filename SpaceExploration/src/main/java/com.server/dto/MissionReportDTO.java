package com.server.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Mission report details")
public class MissionReportDTO {
    private int id;
    private String resultsDescription;
    private boolean isSuccessful;
    private int missionId;
}
