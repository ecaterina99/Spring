package com.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "mission_specializations")
public class MissionSpecialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false, unique = true)
    @JsonBackReference("mission-specializations")
    private Mission mission;


    public enum Specialization {
        PILOT("Pilot"),
        DOCTOR("Doctor"),
        SCIENTIST("Scientist"),
        GEOLOGIST("Geologist"),
        ENGINEER("Engineer");

        private final String displayName;

        Specialization(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Specialization fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            String normalized = value.trim().toUpperCase();

            try {
                return Specialization.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + value +
                        ". Valid values are: pilot, doctor, scientist, geologist, engineer ");
            }
        }
    }

    @NotNull(message = "Specialization is required")
    @Schema(description = "Required specialization type",
            allowableValues = {"PILOT", "ENGINEER", "SCIENTIST", "DOCTOR", "GEOLOGIST"})
    @Column(name = "specialization", nullable = false)
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10, message = "Quantity cannot exceed 10")
    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

}
