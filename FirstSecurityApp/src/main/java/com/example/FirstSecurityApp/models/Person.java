package com.example.FirstSecurityApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "This field can't be empty")
    @Column(name = "name")
    private String name;


    @Column(name = "password")
    private String password;

    @Min(value = 1900, message = "year must be bigger than 1900")
    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Column(name = "role")
    private String role;

    public Person() {}

    public Person(String name, String password, Integer yearOfBirth, String role) {
        this.name = name;
        this.password = password;
        this.yearOfBirth = yearOfBirth;
        this.role = role;

    }

    @Override
    public String toString() {
        return "person{"+
                "id="+id+
                ", username="+ name+
                ", year of birth="+yearOfBirth+
                ", role="+role+
                ", password="+ password+"}";
    }
}
