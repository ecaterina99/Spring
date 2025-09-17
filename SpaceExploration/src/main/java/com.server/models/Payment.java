package com.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Initial budget is required")
    @Min(value = 0, message = "Initial budget cannot be negative")
    @Column(name="initial_budget", nullable = false)
    private int initialBudget;

    @NotNull
    @Column(name="proceeds", nullable = false)
    private int proceeds;

    @NotNull(message = "Final budget is required")
    @Column(name="final_budget", nullable = false)
    private int finalBudget;

    @NotNull(message = "Mission payment is required")
    @Column(name="mission_payment", nullable = false)
    private int missionPayment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false, unique = true)
    @JsonBackReference("mission-payment")
    private Mission mission;

}
