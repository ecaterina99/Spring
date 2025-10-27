package com.server.controllers;

import com.server.dto.UserDTO;
import com.server.models.User;
import com.server.services.BudgetService;
import com.server.services.RegistrationService;
import com.server.util.GlobalApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration and authentication endpoints")
@RequiredArgsConstructor
public class AuthController implements GlobalApiResponses {

    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final BudgetService budgetService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register a new user", description = "Creates a new user account and initializes their budget")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "User successfully registered"
            )})
    public void register(@Valid @RequestBody UserDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        User savedUser = registrationService.register(user);
        budgetService.createInitialBudget(savedUser.getId());
    }
    @Operation(
            summary = "Authenticate user",
            description = "Authenticates user credentials and returns a JWT access token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )
            )})
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(req.email())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("roles", List.of("USER"))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenResponse(token);
    }
}
@Schema(description = "Login request with user credentials")
record LoginRequest(@Schema(description = "User email address", example = "user@example.com")
                            String email,
                            @Schema(description = "User password", example = "SecurePass123")
                            String password) {}
@Schema(description = "JWT token response")
record TokenResponse( @Schema(description = "JWT access token", example = "eyJhbGciOiJSUzI1NiJ9...")String accessToken) {}