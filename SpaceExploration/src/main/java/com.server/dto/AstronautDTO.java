package com.server.dto;

import com.server.models.Astronaut;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
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
}
