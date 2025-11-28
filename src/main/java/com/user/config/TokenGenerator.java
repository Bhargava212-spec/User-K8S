package com.user.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenGenerator {

    @Value("${expiration.millis}")
    private Integer expirationMillis;

    @Value("${jwt.secretKey}")
    private String jwtSecret;


    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() + expirationMillis)).
                signWith(SignatureAlgorithm.HS256, jwtSecret).
                compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).
                    parseClaimsJws(token).
                    getBody();

            return claims.getSubject().equals(userDetails.getUsername())
                    && isTokenValid(claims);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenValid(Claims claims) {
        return claims.getExpiration().after(new Date());
    }


    public String extractUsername(String jwt) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).
                    parseClaimsJws(jwt).
                    getBody().getSubject();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            return null;
        }
    }
}
