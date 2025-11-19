package com.example.travel_destinations.security;

import com.example.travel_destinations.security.AuthRequest;
import com.example.travel_destinations.security.AuthResponse;
import com.example.travel_destinations.security.CustomUserDetailsService;
import com.example.travel_destinations.security.util.JwtUtil;
import javax.swing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller для аутентифікації користувачів
 * Логін користувачів
 * Генерація токенів
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    //POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // аутентифікація через Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            // невірні дані для входу
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect username or password");
        }

        // завантажуємо деталі користувача
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getUsername());

        // отримуємо роль користувача
        final String role = userDetailsService.getUserRole(authRequest.getUsername());

        // генеруємо токен
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), role);


        return ResponseEntity.ok(new AuthResponse(jwt, authRequest.getUsername(), role));
    }



}