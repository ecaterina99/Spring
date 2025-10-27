package com.server.controllers;

import com.server.dto.UserDTO;
import com.server.models.User;
import com.server.repositories.UserRepository;
import com.server.util.GlobalApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations for retrieving current user information")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")

public class UserController implements GlobalApiResponses {

    private final UserRepository userRepository;

    @GetMapping("/")
    @Operation(
            summary = "Get current authenticated user",
            description = "Retrieves the profile information of the currently authenticated user based on JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User information retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            )
    })
    public UserDTO getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                null,
                user.getRole()
        );
    }
}