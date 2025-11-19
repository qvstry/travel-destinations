package com.example.travel_destinations.security;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервіс для управління користувачами
 * Hardcoded користувачі:
 * 1. admin / admin123 - ROLE_ADMIN (повний доступ, також лише для адміна доступ до тревелера)
 * 2. manager / manager123 - ROLE_MANAGER (створення/редагування)
 * 3. user / user123 - ROLE_USER (тільки перегляд)
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // encoder для хешування паролів
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Map<String, UserInfo> users = new HashMap<>();

    public CustomUserDetailsService() {
        // Додаємо користувачів
        users.put("admin", new UserInfo(
                "admin",
                passwordEncoder.encode("admin123"),
                "ROLE_ADMIN"
        ));
        users.put("user", new UserInfo(
                "user",
                passwordEncoder.encode("user123"),
                "ROLE_USER"
        ));
        users.put("manager", new UserInfo(
                "manager",
                passwordEncoder.encode("manager123"),
                "ROLE_MANAGER"
        ));
    }

    //завантажує деталі користувача при аутентифікації
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //шукаємо користувача
        UserInfo userInfo = users.get(username);


        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // повертаємо UserDetails
        return User.builder()
                .username(userInfo.getUsername())
                .password(userInfo.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(userInfo.getRole())
                ))
                .build();
    }

    //oтримує роль користувача за username при генерації токена

    public String getUserRole(String username) {
        UserInfo userInfo = users.get(username);
        return userInfo != null ? userInfo.getRole() : null;
    }

    //внутрішній клас для зберігання інформації про користувача

    private static class UserInfo {
        private final String username;
        private final String password;
        private final String role;

        public UserInfo(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }
}