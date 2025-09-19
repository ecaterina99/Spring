package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Astronaut;
import com.server.models.Mission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Astronauts information")
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class AstronautDTO {
    private int id;
    private String fullName;
    private Integer yearsOfExperience;
    private String phone;
    private LocalDate dateOfBirth;
    private Integer dailyRate;
    private Integer fitnessScore;
    private Integer educationScore;
    private Integer psychologicalScore;
    private Integer overallScore;
    private String image;
    private Astronaut.Specialization specialization;
    private Astronaut.HealthStatus healthStatus;
    @Builder.Default
    private List<MissionDTO.MissionSummaryDTO> missions = new ArrayList<>();
    @Builder.Default
    private List<MissionDTO.MissionWithoutDetailsDTO> passedMissions = new ArrayList<>();


    public static AstronautDTO withMissions(Astronaut astronaut) {
        return AstronautDTO.builder()
                .id(astronaut.getId())
                .fullName(astronaut.getFullName())
                .yearsOfExperience(astronaut.getYearsOfExperience())
                .phone(astronaut.getPhone())
                .dateOfBirth(astronaut.getDateOfBirth())
                .dailyRate(astronaut.getDailyRate())
                .fitnessScore(astronaut.getFitnessScore())
                .educationScore(astronaut.getEducationScore())
                .psychologicalScore(astronaut.getPsychologicalScore())
                .overallScore(astronaut.getOverallScore())
                .image(astronaut.getImage())
                .specialization(astronaut.getSpecialization())
                .healthStatus(astronaut.getHealthStatus())
                .missions(
                        astronaut.getMissionParticipants().stream()
                                .map(mp -> {
                                    Mission mission = mp.getMission();

                                    List<MissionDTO.MissionSummaryDTO.SpecializationSummaryDTO> specs =
                                            mission.getMissionRequiredSpecializations().stream()
                                                    .map(mrs -> new MissionDTO.MissionSummaryDTO.SpecializationSummaryDTO(
                                                            mrs.getSpecialization(),
                                                            mrs.getQuantityRequired()
                                                    ))
                                                    .toList();

                                    MissionDTO.MissionSummaryDTO summaryDTO = new MissionDTO.MissionSummaryDTO();
                                    summaryDTO.setId(mission.getId());
                                    summaryDTO.setMissionName(mission.getMissionName());
                                    summaryDTO.setDifficultyLevel(mission.getDifficultyLevel());
                                    summaryDTO.setDestinationName(mission.getDestination().getDestinationName());
                                    summaryDTO.setSuccessful(mission.getMissionReport() != null &&
                                            mission.getMissionReport().isSuccessful());
                                    summaryDTO.setResultsDescription(
                                            mission.getMissionReport() != null ?
                                                    mission.getMissionReport().getResultsDescription() : ""
                                    );
                                    summaryDTO.setSpecializations(specs);

                                    return summaryDTO;
                                })
                                .toList()
                )
                .build();
    }

    public static AstronautDTO withMissionWithoutDetails(Astronaut astronaut) {
        return AstronautDTO.builder()
                .id(astronaut.getId())
                .fullName(astronaut.getFullName())
                .yearsOfExperience(astronaut.getYearsOfExperience())
                .phone(astronaut.getPhone())
                .dateOfBirth(astronaut.getDateOfBirth())
                .dailyRate(astronaut.getDailyRate())
                .fitnessScore(astronaut.getFitnessScore())
                .educationScore(astronaut.getEducationScore())
                .psychologicalScore(astronaut.getPsychologicalScore())
                .overallScore(astronaut.getOverallScore())
                .image(astronaut.getImage())
                .specialization(astronaut.getSpecialization())
                .healthStatus(astronaut.getHealthStatus())
                .passedMissions(
                        astronaut.getMissionParticipants().stream()
                                .map(mp -> {
                                    Mission mission = mp.getMission();
                                    MissionDTO.MissionWithoutDetailsDTO summaryDTO =
                                            new MissionDTO.MissionWithoutDetailsDTO();
                                    summaryDTO.setMissionName(mission.getMissionName());
                                    return summaryDTO;
                                })
                                .toList()
                )
                .build();
    }
}