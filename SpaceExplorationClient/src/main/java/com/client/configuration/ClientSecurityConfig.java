package com.client.configuration;
import com.client.helpers.ApiAuthProvider;
import com.client.service.TokenStorage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
public class ClientSecurityConfig {

    private final ApiAuthProvider authProvider;
    private final TokenStorage tokenStorage;

    public ClientSecurityConfig(ApiAuthProvider authProvider,  @Lazy TokenStorage tokenStorage) {
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
                        .addLogoutHandler(new LogoutHandler() {
                            @Override
                            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                                tokenStorage.clear();
                            }
                        })
                        .permitAll()
                )
                .authenticationProvider(authProvider);

        return http.build();
    }
}