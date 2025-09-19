package com.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity(name = "mission_participants")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    @JsonBackReference("mission-participants")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astronaut_id", nullable = false)
    @JsonBackReference("astronaut-participants")
    private Astronaut astronaut;


}
