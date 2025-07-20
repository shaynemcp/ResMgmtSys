package com.umgc.swen646;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 * Utility class for JWT token operations.
 */
public class JWTUtil {
    private static final String SECRET_KEY = "your-super-secret-jwt-key-that-is-at-least-256-bits-long";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    /**
     * Generates a JWT token for a user after successful authentication.
     * @param userId User ID
     * @param username Username
     * @return JWT token string
     */
    public static String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith((SecretKey) key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Validates a JWT token and returns the claims if valid.
     * @param token JWT token string
     * @return Claims object if valid, null if invalid
     */
    public static Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Extracts user ID from a JWT token.
     * @param token JWT token string
     * @return User ID if valid, null if invalid
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        if (claims != null) {
            return claims.get("userId", Long.class);
        }
        return null;
    }

    /**
     * Extracts username from a JWT token.
     * @param token JWT token string
     * @return Username if valid, null if invalid
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = validateToken(token);
        if (claims != null) {
            return claims.get("username", String.class);
        }
        return null;
    }

    /**
     * Checks if a JWT token is expired.
     * @param token JWT token string
     * @return true if expired, false if valid
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
} 