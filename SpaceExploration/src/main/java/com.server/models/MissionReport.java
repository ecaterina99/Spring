package com.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "mission_reports")
public class MissionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Results description is required")
    @Size(min = 2,  message = "Results description must be longer")
    @Column(name = "results_description", nullable = false)
    private String resultsDescription;

    @Column(name="is_successful", nullable = false)
    private boolean isSuccessful;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false, unique = true)
    @JsonBackReference("mission-report")
    private Mission mission;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    @JsonManagedReference("report-participants")
    private List<MissionParticipants> participants = new ArrayList<>();

}