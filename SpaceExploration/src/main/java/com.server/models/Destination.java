package com.server.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "destination")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Destination name is required")
    @Size(min = 2, max = 100, message = "Destination name must be between 2 and 100 characters")
    @Column(name = "destination_name", nullable = false)
    private String destinationName;

    @NotBlank(message = "Distance from earth is required")
    @Size(min = 2, max = 50, message = "Destination name must be between 2 and 50 characters")
    @Column(name = "distance_from_earth", nullable = false)
    private String distanceFromEarth;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 500, message = "Destination name must be between 2 and 500 characters")
    @Column(name = "description", nullable = false)
    private String description;

    @Column (name = "is_habitable", nullable = false)
    private boolean isHabitable;

    @Column(name = "gravity")
    private Integer gravity;

    @Column(name = "image_url")
    private String image;

    public enum EntityType {
        PLANET("Planet"),
        STAR("Star"),
        ASTEROID("Asteroid");

        private final String displayName;

        EntityType(String displayName) {
            this.displayName = displayName;
        }

        public static EntityType fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            String normalized = value.trim().toUpperCase();

            try {
                return EntityType.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + value +
                        ". Valid values are: PLANET, STAR, ASTEROID");
            }
        }
    }
    @NotNull(message = "Entity type is required")
    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType entityType = EntityType.PLANET;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("destination-missions")
    private List<Mission> missions = new ArrayList<>();

}
