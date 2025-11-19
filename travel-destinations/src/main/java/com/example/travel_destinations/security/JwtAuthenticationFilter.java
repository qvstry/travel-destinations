package com.example.travel_destinations.security;

import com.example.travel_destinations.security.util.JwtUtil;
import com.example.travel_destinations.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * @param request HTTP запит
     * @param response HTTP відповідь
     * @param filterChain ланцюг фільтрів
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // витягуємо заголовок Authorization з запиту
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // перевіряємо чи є токен у заголовку та, чи починається він з Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // видаляємо "Bearer " щоб отримати сам токен
            jwt = authorizationHeader.substring(7);

            try {
                // витягуємо юзернейм
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {

                logger.error("JWT Token extraction failed: " + e.getMessage());
            }
        }

        // якщо юзернейс знайдено, а користувач ще не аутентифікований
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // завантажуємо деталі користувача
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            if (jwtUtil.validateToken(jwt, userDetails)) {


                String role = jwtUtil.extractRole(jwt);

                // створюємо об'єкт аутентифікації
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(role))
                        );


                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // встановлюємо аутентифікацію
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //ланцюг фільтрів
        filterChain.doFilter(request, response);
    }
}