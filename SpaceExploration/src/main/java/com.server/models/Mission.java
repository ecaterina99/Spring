package com.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Mission name is required")
    @Size(min = 2, max = 100, message = "Mission name must be between 2 and 100 characters")
    @Column(name = "mission_name", nullable = false)
    private String missionName;

    @NotBlank(message = "Mission code is required")
    @Size(min = 4, max = 6, message = "Mission name must be between 2 and 6 characters")
    @Column(name="code", nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Mission description is required")
    @Column(name="description", nullable = false)
    private String description;

    @Min(value = 1)
    @NotNull
    @Column(name="duration_days", nullable = false)
    private int durationDays;

    @Min(value = 1)
    @NotNull
    @Column(name="crew_size_required", nullable = false)
    private int crewSizeRequired;

    @Min(value = 1)
    @NotNull
    @Column(name="score_value", nullable = false)
    private int scoreValue;

    @NotBlank(message = "Potential issues description is required")
    @Column(name="potential_issues", nullable = false)
    private String potentialIssues;

    @Column(name = "image_url")
    private String image;


    public enum DifficultyLevel {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high"),
        EXTREME("extreme");

        private final String displayName;

        DifficultyLevel(String displayName) {
            this.displayName = displayName;
        }


        public static DifficultyLevel fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            String normalized = value.trim().toUpperCase();

            try {
                return DifficultyLevel.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + value +
                        ". Valid values are: low, medium, high, extreme ");
            }
        }
    }

    @NotNull(message = "Difficulty level is required")
    @Column(name = "difficulty_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    @JsonBackReference("destination-missions")
    private Destination destination;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("mission-participants")
    private List<MissionParticipants> missionParticipants = new ArrayList<>();

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("mission-report")
    private MissionReport missionReport;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("mission-payment")
    private Payment payment;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("mission-requiredSpecializations")
    private Set<MissionRequiredSpecializations> missionRequiredSpecializations = new HashSet<>();

    public void addRequiredSpecialization(MissionRequiredSpecializations.Specialization specialization, int quantity) {
        MissionRequiredSpecializations requiredSpecialization = new MissionRequiredSpecializations();
        requiredSpecialization.setMission(this);
        requiredSpecialization.setSpecialization(specialization);
        requiredSpecialization.setQuantityRequired(quantity);
        this.missionRequiredSpecializations.add(requiredSpecialization);
    }

    public void removeRequiredSpecialization(MissionRequiredSpecializations.Specialization specialization) {
        this.missionRequiredSpecializations.removeIf(rs -> rs.getSpecialization().equals(specialization));
    }
    public int getTotalRequiredCrew() {
        return missionRequiredSpecializations.stream()
                .mapToInt(MissionRequiredSpecializations::getQuantityRequired)
                .sum();
    }

}

