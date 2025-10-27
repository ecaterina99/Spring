package com.client.DTO;

import com.client.enums.AstronautEnums;

import lombok.*;

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
    private AstronautEnums.HealthStatus healthStatus;
    private AstronautEnums.Specialization specialization;
    @Setter
    @Getter
    private String imageUrl;

    public String getFullImageUrl() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "https://ui-avatars.com/api/?name=Astronaut&background=random";
        }
        return String.format("https://cosmo-trail.s3.eu-central-1.amazonaws.com/%s", imageUrl);
    }

}