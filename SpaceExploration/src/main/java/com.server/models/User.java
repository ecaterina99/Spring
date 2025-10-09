package com.server.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "This field can't be empty")
    @Column(name="first_name", nullable = false)
    private String firstName;
    @NotEmpty(message = "This field can't be empty")
    @Column(name="last_name", nullable = false)
    private String lastName;
    @NotEmpty(message = "This field can't be empty")
    @Email(message = "Invalid email format")
    @Column(name="email", nullable = false)
    private String email;
    @NotEmpty(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
   /* @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )

    */
    @Column(name="password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(100) DEFAULT 'USER'")
    private String role;
}
