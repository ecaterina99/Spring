package com.link.EmployeesWebApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "employees")
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    @Id  //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "cnp")
    private String cnp;

    @Column
    private float salary;

    private Integer age;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job mainJob;

    @OneToOne
    @JoinColumn(name = "id_park")
    @JsonManagedReference
    private ParkingLot parkingLot;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    @JoinTable(
            name = "employees_jobs",
            joinColumns = @JoinColumn(name = "id_employee"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> allJobs;


    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
