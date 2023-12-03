package com.ghopital.projet.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    private static final String ALGORITHM = "HS256";
    private static final String DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.forName(ALGORITHM).getValue();
    @Value("${JWT_TOKEN_SECRET}")
    private String jwtSecret;
    @Value("${JWT_ACCESS_TOKEN_EXPIRATION}")
    private long jwtExpirationDate;

    // Generate JWT Token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(decodeSecretKey(jwtSecret), SignatureAlgorithm.forName(DEFAULT_SIGNATURE_ALGORITHM))
                .compact();
    }

    // Validate JWT Token
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(decodeSecretKey(jwtSecret))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Get username from JWT Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Get expiration date from JWT Token
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if JWT Token is expired
    public boolean isTokenExpired(String token) {
        Date expirationDate = extractExpirationDate(token);
        if (expirationDate == null) {
            return false;
        }
        Date currentDate = new Date();
        return expirationDate.before(currentDate);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(decodeSecretKey(jwtSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Key decodeSecretKey(String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

}
