package model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class User {
    private String userName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date_of_birth;
    private String email;

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
