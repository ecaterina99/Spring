package com.server.dto;

import com.server.models.Astronaut;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private List<MissionDTO.MissionSummaryDTO> missions;

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
                        .map(mp -> new MissionDTO.MissionSummaryDTO(
                                mp.getMission().getId(),
                                mp.getMission().getMissionName(),
                                mp.getMission().getDifficultyLevel(),
                                mp.getMission().getDestination().getDestinationName(),
                                mp.getMission().getMissionReport().isSuccessful(),
                                mp.getMission().getMissionReport().getResultsDescription()
                        ))
                        .toList());

    }
}
