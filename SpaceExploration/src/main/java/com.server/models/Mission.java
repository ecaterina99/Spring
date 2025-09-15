package com.server.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name="code", nullable = false)
    private String code;

    @NotBlank(message = "Mission description is required")
    @Column(name="description", nullable = false)
    private String description;

    @Pattern(regexp = "^[+]?[0-9]+$", message = "Invalid number format")
    @NotNull
    @Column(name="duration_days", nullable = false)
    private int durationDays;


    @Pattern(regexp = "^[+]?[0-9]+$", message = "Invalid number format")
    @NotNull
    @Column(name="crew_size_required", nullable = false)
    private int crewSizeRequired;

    @NotBlank(message = "Please enter required specializations")
    @Column(name="required_specializations", nullable = false)
    private String requiredSpecializations;

    @Pattern(regexp = "^[+]?[0-9]+$", message = "Invalid number format")
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


    @Pattern(regexp = "^[+]?[0-9]{1,3}$", message = "Invalid number format")
    @NotNull
    @Column(name="destination_id", nullable = false)
    private int destinationId;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("mission_participants")
    private List<MissionParticipants> missionParticipants = new ArrayList<>();

}
