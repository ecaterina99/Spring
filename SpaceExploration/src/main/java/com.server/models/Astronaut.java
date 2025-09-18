package com.server.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "astronauts")
public class Astronaut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 50, message = "Years of experience cannot exceed 50")
    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @Pattern(regexp = "^[+]?[0-9]{8,10}$", message = "Invalid phone number format")
    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;


    @NotNull(message = "Daily rate is required")
    @DecimalMin(value = "200.0", message = "Daily rate must be at least 200")
    @DecimalMax(value = "2000.0", message = "Daily rate cannot exceed 2000")
    @Column(name = "daily_rate", nullable = false)
    private Integer dailyRate;

    @Min(value = 0, message = "Fitness score must be between 0 and 100")
    @Max(value = 100, message = "Fitness score must be between 0 and 100")
    @Column(name = "fitness_score", nullable = false)
    private Integer fitnessScore;

    @Min(value = 0, message = "Education score must be between 0 and 100")
    @Max(value = 100, message = "Education score must be between 0 and 100")
    @Column(name = "education_score", nullable = false)
    private Integer educationScore;

    @Min(value = 0, message = "Psychological score must be between 0 and 100")
    @Max(value = 100, message = "Psychological score must be between 0 and 100")
    @Column(name = "psychological_score", nullable = false)
    private Integer psychologicalScore;

    @Column(name = "overall_score")
    private Integer overallScore;

    @PrePersist
    @PreUpdate
    public void calculateOverallScore() {
        if (fitnessScore != null && educationScore != null && psychologicalScore != null) {
            this.overallScore = (fitnessScore + educationScore + psychologicalScore) / 3;
        }
    }

    @Column(name = "image_url")
    private String image;

    public enum HealthStatus {
        FLIGHT_READY("Flight Ready"),
        MEDICAL_REVIEW("Medical Review"),
        RETIRED("Retired");

        private final String displayName;

        HealthStatus(String displayName) {
            this.displayName = displayName;
        }


        public static HealthStatus fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            String normalized = value.trim().toUpperCase().replace(" ", "_");

            try {
                return HealthStatus.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + value +
                        ". Valid values are: FLIGHT_READY, MEDICAL_REVIEW, RETIRED ");
            }
        }
    }

    @NotNull(message = "Health status is required")
    @Column(name = "health_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus = HealthStatus.FLIGHT_READY;


    public enum Specialization {
        PILOT("Pilot"),
        ENGINEER("Engineer"),
        SCIENTIST("Scientist"),
        DOCTOR("Doctor"),
        GEOLOGIST("Geologist");

        private final String displayName;

        Specialization(String displayName) {
            this.displayName = displayName;
        }

        public static Specialization fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            String normalized = value.trim().toUpperCase();

            try {
                return Specialization.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid specialization: " + value +
                        ". Valid values are: PILOT, ENGINEER, SCIENTIST, DOCTOR, GEOLOGIST ");
            }
        }
    }

    @NotNull(message = "Specialization is required")
    @Column(name = "specialization", nullable = false)
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @OneToMany(mappedBy = "astronaut", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("astronaut-participants")
    private List<MissionParticipants> missionParticipants = new ArrayList<>();

}
