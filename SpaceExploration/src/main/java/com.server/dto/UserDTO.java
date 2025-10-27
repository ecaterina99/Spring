package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information about user")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class UserDTO {
    private int id;
    @NotBlank
    @Schema(description = "First name", example = "John")
    @Size(min = 2, message = "First name must have at least 2 characters")
    private String firstName;
    @Schema(description = "Last name", example = "Black")
    @NotBlank
    @Size(min = 2, message = "Last name must have at least 2 characters")
    private String lastName;
    @Schema(description = "Email", example = "john@gmail.com")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @Schema(description = "User's password (write-only, excluded from responses)", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;
    private String role;
}
