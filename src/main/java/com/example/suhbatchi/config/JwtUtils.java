package com.example.suhbatchi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    // Ensure the secret is long enough for HS256 (e.g., at least 256 bits)
    private final Key jwtSigningKey = Keys.hmacShaKeyFor("SecretKeySecretKeySecretKeySecretKey".getBytes(StandardCharsets.UTF_8));

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract a claim using a resolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Checks if a specific claim exists
    public boolean hasClaim(String token, String claimName) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }

    // Updated method using parserBuilder()
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSigningKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateTemporaryToken(String phoneNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("temp", true);
        claims.put("refresh", false);
        return createToken(claims, phoneNumber);
    }

    public String generatePermanentToken(String clientId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("temp", false);
        claims.put("refresh", false);
        return createToken(claims, clientId);
    }

    public String refreshToken(String clientId,String phoneNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("refresh", true);
        claims.put("temp", false);
        claims.put("clientId", clientId);
        claims.put("phoneNumber", phoneNumber);
        return createRefreshToken(claims, clientId);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)))
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    // Checks if the token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).after(new Date());
    }

    public boolean isTemporaryToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.containsKey("temp") && (Boolean) claims.get("temp");
    }

    public boolean isActiveUser(String token) {
        Claims claims = extractAllClaims(token);
        return claims.containsKey("active") && (Boolean) claims.get("active");
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.containsKey("refresh") && (Boolean) claims.get("refresh");
    }
}
