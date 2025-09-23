package com.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

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
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Mission description is required")
    @Column(name = "description", nullable = false)
    private String description;

    @Min(value = 1)
    @NotNull
    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Min(value = 1)
    @NotNull
    @Column(name = "payment_amount", nullable = false)
    private int paymentAmount;

    @Min(value = 1)
    @NotNull
    @Column(name = "crew_size_required", nullable = false)
    private int crewSizeRequired;

    @Min(value = 1)
    @NotNull
    @Column(name = "score_value", nullable = false)
    private int scoreValue;

    @NotBlank(message = "Potential issues description is required")
    @Column(name = "potential_issues", nullable = false)
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
    @JsonManagedReference("mission-specializations")
    private Set<MissionSpecialization> missionSpecializations = new HashSet<>();

    public void addRequiredSpecialization(MissionSpecialization.Specialization specialization, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > 10) {
            throw new IllegalArgumentException("Maximum 10 specialists per specialization");
        }

        Optional<MissionSpecialization> existingSpec = findSpecialization(specialization);
        if (existingSpec.isPresent()) {
            existingSpec.get().setQuantity(quantity);
        } else {
            MissionSpecialization newSpec = MissionSpecialization.builder()
                    .mission(this)
                    .specialization(specialization)
                    .quantity(quantity)
                    .build();
            this.missionSpecializations.add(newSpec);
        }
    }

    public void removeRequiredSpecialization(MissionSpecialization.Specialization specialization) {
        this.missionSpecializations.removeIf(rs ->
                rs.getSpecialization().equals(specialization));
    }

    public void updateSpecializationQuantity(MissionSpecialization.Specialization specialization, int newQuantity) {
        if (newQuantity <= 0) {
            removeRequiredSpecialization(specialization);
            return;
        }

        findSpecialization(specialization)
                .ifPresentOrElse(
                        spec -> spec.setQuantity(newQuantity),
                        () -> addRequiredSpecialization(specialization, newQuantity)
                );
    }


    private Optional<MissionSpecialization> findSpecialization(MissionSpecialization.Specialization specialization) {
        return missionSpecializations.stream()
                .filter(rs -> rs.getSpecialization().equals(specialization))
                .findFirst();
    }

    public int getTotalRequiredCrew() {
        return missionSpecializations.stream()
                .mapToInt(MissionSpecialization::getQuantity)
                .sum();
    }

    public boolean hasSpecialization(MissionSpecialization.Specialization specialization) {
        return findSpecialization(specialization).isPresent();
    }

    public int getRequiredQuantityForSpecialization(MissionSpecialization.Specialization specialization) {
        return findSpecialization(specialization)
                .map(MissionSpecialization::getQuantity)
                .orElse(0);
    }

    public boolean isCrewRequirementMet() {
        return getTotalRequiredCrew() <= this.crewSizeRequired;
    }
}



