package com.client.DTO;

import com.client.enums.AstronautEnums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AstronautDTO {
    private int id;
    private String firstName;
    private String lastName;
    private Integer yearsOfExperience;
    private String phone;
    private LocalDate dateOfBirth;
    private Double dailyRate;
    private Integer fitnessScore;
    private Integer educationScore;
    private Integer psychologicalScore;
    private Integer overallScore;
    private String imageUrl;
    private AstronautEnums.HealthStatus healthStatus;
    private AstronautEnums.Specialization specialization;
}