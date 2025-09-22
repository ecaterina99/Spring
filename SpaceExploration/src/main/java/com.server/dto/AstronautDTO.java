package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Astronaut;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Astronauts information")
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class AstronautDTO {

    private int id;
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;
    @NotNull(message = "Years of experience are required")
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 50, message = "Years of experience cannot exceed 50")
    private Integer yearsOfExperience;
    @Pattern(regexp = "^[+]?[0-9]{8,10}$", message = "Invalid phone number format")
    @NotNull
    private String phone;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    @NotNull(message = "Daily rate is required")
    @DecimalMin(value = "200.0", message = "Daily rate must be at least 200")
    @DecimalMax(value = "2000.0", message = "Daily rate cannot exceed 2000")
    private Double dailyRate;
    @Min(value = 0, message = "Fitness score must be between 0 and 100")
    @Max(value = 100, message = "Fitness score must be between 0 and 100")
    private Integer fitnessScore;
    @Min(value = 0, message = "Education score must be between 0 and 100")
    @Max(value = 100, message = "Education score must be between 0 and 100")
    private Integer educationScore;
    @Min(value = 0, message = "Psychological score must be between 0 and 100")
    @Max(value = 100, message = "Psychological score must be between 0 and 100")
    private Integer psychologicalScore;
    @Min(value = 0, message = "Overall score must be between 0 and 100")
    @Max(value = 100, message = "Overall score must be between 0 and 100")
    private Integer overallScore;
    private String imageUrl;
    @NotNull(message = "Specialization is required")
    private Astronaut.Specialization specialization;
    @NotNull(message = "Health status is required")
    private Astronaut.HealthStatus healthStatus;

}



   /* public static AstronautDTO astronautDetails(Astronaut astronaut) {
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
                .build();
    }
    */