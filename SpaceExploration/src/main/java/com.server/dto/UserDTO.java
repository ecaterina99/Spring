package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User information")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class UserDTO {
    private int id;
    @NotBlank(message = "First name is required")
    @Size(min = 2, message = "First name must have at least 2 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, message = "Last name must have at least 2 characters")
    private String lastName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;
    private String role;

}
