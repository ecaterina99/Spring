package com.server.dto;

import com.server.models.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Mission information")
public class MissionDTO {
    private int id;
    private String missionName;
    private String code;
    private String description;
    private int durationDays;
    private int crewSizeRequired;
    private String requiredSpecializations;
    private int scoreValue;
    private String potentialIssues;
    private String image;
    private Mission.DifficultyLevel difficultyLevel;
    private int destinationId;
    private List<MissionParticipants> missionParticipants;

}
