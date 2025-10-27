package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Astronaut;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Information about astronauts")

public class AstronautDTO {
    @Schema(description = "Astronaut ID", example = "1")
    private int id;
    @NotBlank
    @Schema(description = "First name of the astronaut", example = "Neil")
    private String firstName;
    @NotBlank
    @Schema(description = "Last name of the astronaut", example = "Armstrong")
    private String lastName;
    @NotNull
    @Schema(description = "Years of experience", example = "5")
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 50, message = "Years of experience cannot exceed 50")
    private Integer yearsOfExperience;
    @Pattern(regexp = "^[+]?[0-9]{8,10}$", message = "Invalid phone number format")
    @NotNull
    @Schema(description = "Phone", example = "069703888")
    private String phone;
    @NotNull
    @Schema(description = "Date of birth", example = "1965-03-19")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    @NotNull()
    @Schema(description = "Daily rate", example = "200")
    @Min(value = 150, message = "Daily rate must be at least 150")
    @Max(value = 2000, message = "Daily rate cannot exceed 2000")
    private Integer dailyRate;
    @Schema(description = "Fitness score", example = "50")
    @Min(value = 0, message = "Fitness score must be between 0 and 100")
    @Max(value = 100, message = "Fitness score must be between 0 and 100")
    private Integer fitnessScore;
    @Schema(description = "Education score", example = "60")
    @Min(value = 0, message = "Education score must be between 0 and 100")
    @Max(value = 100, message = "Education score must be between 0 and 100")
    private Integer educationScore;
    @Schema(description = "Psychological score", example = "70")
    @Min(value = 0, message = "Psychological score must be between 0 and 100")
    @Max(value = 100, message = "Psychological score must be between 0 and 100")
    private Integer psychologicalScore;
    @Schema(description = "Overall score", example = "60")
    @Min(value = 0, message = "Overall score must be between 0 and 100")
    @Max(value = 100, message = "Overall score must be between 0 and 100")
    private Integer overallScore;
    @Setter
    @Getter
    @Schema(description = "Image path", example = "1.jpg")
    private String imageUrl;
    @Schema(description = "Specialization", example = "PILOT")
    @NotNull(message = "Specialization is required")
    private Astronaut.Specialization specialization;
    @Schema(description = "Astronaut health status", example = "FLIGHT_READY")
    @NotNull(message = "Health status is required")
    private Astronaut.HealthStatus healthStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Astronaut update information - all fields are optional for partial updates")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class AstronautUpdateDTO {
        @Schema(description = "First name of the astronaut", example = "Neil")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        private String firstName;
        @Schema(description = "Last name of the astronaut", example = "Armstrong")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        private String lastName;
        @Schema(description = "Years of experience", example = "5")
        @Min(value = 0, message = "Years of experience cannot be negative")
        @Max(value = 50, message = "Years of experience cannot exceed 50")
        private Integer yearsOfExperience;
        @Schema(description = "Phone", example = "069703888")
        @Pattern(regexp = "^[+]?[0-9]{8,10}$", message = "Invalid phone number format")
        private String phone;
        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;
        @Min(value = 150, message = "Daily rate must be at least 200")
        @Max(value = 1000, message = "Daily rate cannot exceed 1000")
        private Integer dailyRate;
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
        @Schema(description = "Image path", example = "1.jpg")
        private String imageUrl;
        private Astronaut.Specialization specialization;
        private Astronaut.HealthStatus healthStatus;
    }
}

