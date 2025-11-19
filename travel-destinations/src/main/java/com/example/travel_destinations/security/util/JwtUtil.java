package com.example.travel_destinations.security.util;

//import java.util.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // application.properties
    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expiration}")
    private Long expiration;

    //витягуємо username, expiration, user ole з JWT токена

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Перевіряємо чи токен не прострочений
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Генерує новий токен для користувача

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    //Створює JWT токен з додатковими даними

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
                .setClaims(claims)              // role
                .setSubject(subject)            // Username
                .setIssuedAt(now)               // Час створення
                .setExpiration(expiryDate)      // Час закінчення
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  // Підпис
                .compact();
    }

    // валідність токена
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //генерує секретний ключ для підпису

  private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}