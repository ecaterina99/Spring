package com.client.helpers;

import com.client.service.ApiClient;
import com.client.service.TokenStorage;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiAuthProvider implements AuthenticationProvider {

    private final ApiClient apiClient;
    private final TokenStorage tokenStorage;

    public ApiAuthProvider(ApiClient apiClient, TokenStorage tokenStorage) {
        this.apiClient = apiClient;
        this.tokenStorage = tokenStorage;
    }

    @Override
    public Authentication authenticate(Authentication auth) {
        String email = auth.getName();
        String password = auth.getCredentials().toString();

        try {
            String token = apiClient.login(email, password);

            tokenStorage.setToken(token);

            return new UsernamePasswordAuthenticationToken(
                    email, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}