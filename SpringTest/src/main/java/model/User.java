package model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class User {
    @NotEmpty
    private String userName;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date_of_birth;


    @NotEmpty
    @Email
    private String email;

    public User() {
    }

    public User(String userName, LocalDate date_of_birth, String email) {
        this.userName = userName;
        this.date_of_birth = date_of_birth;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
