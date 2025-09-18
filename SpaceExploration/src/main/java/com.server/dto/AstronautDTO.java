package com.server.dto;

import com.server.models.Astronaut;
import com.server.models.Mission;
import com.server.models.MissionRequiredSpecializations;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Astronauts information")
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
 //   private List<MissionDTO.MissionSummaryDTO> missions;
 private List<MissionDTO.MissionSummaryDTO> missions = new ArrayList<>();



    public static AstronautDTO withMissions(Astronaut astronaut) {
        return new AstronautDTO(astronaut.getId(),
                astronaut.getFullName(),
                astronaut.getYearsOfExperience(),
                astronaut.getPhone(),
                astronaut.getDateOfBirth(),
                astronaut.getDailyRate(),
                astronaut.getFitnessScore(),
                astronaut.getEducationScore(),
                astronaut.getPsychologicalScore(),
                astronaut.getOverallScore(),
                astronaut.getImage(),
                astronaut.getSpecialization(),
                astronaut.getHealthStatus(),
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
                            summaryDTO.setSuccessful(mission.getMissionReport() != null && mission.getMissionReport().isSuccessful());
                            summaryDTO.setResultsDescription(mission.getMissionReport() != null ? mission.getMissionReport().getResultsDescription() : "");
                            summaryDTO.setSpecializations(specs);

                            return summaryDTO;
                        })
                        .toList()
        );
    }

    public static AstronautDTO withoutMissions(Astronaut astronaut) {
        return new AstronautDTO(
                astronaut.getId(),
                astronaut.getFullName(),
                astronaut.getYearsOfExperience(),
                astronaut.getPhone(),
                astronaut.getDateOfBirth(),
                astronaut.getDailyRate(),
                astronaut.getFitnessScore(),
                astronaut.getEducationScore(),
                astronaut.getPsychologicalScore(),
                astronaut.getOverallScore(),
                astronaut.getImage(),
                astronaut.getSpecialization(),
                astronaut.getHealthStatus(),
                new ArrayList<>()
        );
    }
}
