package com.example.travel_destinations.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.travel_destinations.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Spring Security Configuration
 * ендпоінти без аутентифікації trip/destination public, auth
 * ендпоінти з захистом за ролями, adnin/manager, traveler лише для адміна
 * JWT фільтр для перевірки токенів
 * Authentication manager
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //налаштування ланцюга за доступом
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())

                // правила авторизації
                .authorizeHttpRequests(auth -> auth
                                // публічні ендпоінти
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/destinations/public/**").permitAll()
                                .requestMatchers("/api/trips/public/**").permitAll()
                                // з перевіркою ролей
                                 .requestMatchers("/api/destinations/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/trips/admin/**").hasRole("ADMIN")
                                 .requestMatchers("/api/travelers/**").hasRole("ADMIN") //доступ до людей лише в адміна

                                .requestMatchers("/api/destinations/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/api/trips/manager/**").hasAnyRole("ADMIN", "MANAGER")

                                // всі інші запити потребують аутентифікації
                                .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // додавання JWT фільтра перед стандартним фільтром аутентифікації
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // аутентифікація користувачів
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}