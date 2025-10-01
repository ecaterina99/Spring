package com.client.DTO;

import com.client.enums.MissionEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionDTO {
    private int id;
    private String name;
    private String code;
    private String description;
    private Integer durationDays;
    private Integer crewSize;
    private Integer scoreValue;
    private String potentialIssues;
    private String imageUrl;
    private String destinationName;
    private Integer paymentAmount;
    private MissionEnums.DifficultyLevel difficultyLevel;
    private List<MissionSpecializationsDTO> specializations = new ArrayList<>();

}

