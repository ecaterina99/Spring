package com.example.shop.configuration;

import com.example.shop.helpers.CartEventHandler;
import com.example.shop.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    private final PersonDetailsService personDetailsService;
    private final CartEventHandler cartEventHandler;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, CartEventHandler cartEventHandler) {
        this.personDetailsService = personDetailsService;
        this.cartEventHandler = cartEventHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/cart/add", "/cart/update", "/cart/remove", "/cart/count", "/cart/checkout")
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin/**", "/sale/**", "/users/**").hasRole("ADMIN")
                        .requestMatchers("/auth/login", "/auth/register", "/auth/process-register").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("*.png", "*.jpg", "*.jpeg", "*.avif").permitAll()
                        .requestMatchers("/cart/checkout").permitAll()
                        .requestMatchers("/cart/**").permitAll()
                        .requestMatchers("/home", "/contact").permitAll()
                        .requestMatchers("/bouquets", "/bouquets/**").permitAll()
                        .requestMatchers("/plants", "/plants/**").permitAll()
                        .requestMatchers("/gifts", "/gifts/**").permitAll()
                        .requestMatchers("/info/{id}").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/innerFolder/**").permitAll()
                        .anyRequest().hasAnyRole("ADMIN", "USER")
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/process-login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(cartEventHandler)
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(cartEventHandler)
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(personDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}