package com.client.configuration;
import com.client.helpers.ApiAuthProvider;
import com.client.service.TokenStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ClientSecurityConfig {

    private final ApiAuthProvider authProvider;
    private final TokenStorage tokenStorage;

    public ClientSecurityConfig(ApiAuthProvider authProvider, TokenStorage tokenStorage) {
        this.authProvider = authProvider;
        this.tokenStorage = tokenStorage;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/astronauts", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .addLogoutHandler((request, response, auth) -> tokenStorage.clear())
                        .permitAll()
                )
                .authenticationProvider(authProvider);

        return http.build();
    }
}