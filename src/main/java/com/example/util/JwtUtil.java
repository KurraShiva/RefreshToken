//package com.example.util;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtParser;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    String secretKey = "secret123456778901234567891234567890";  // Note: Use a strong key and move it to application.properties in production
//
//    public String generateAccessToken(String username, String role) {
//        return Jwts.builder()
//            .setSubject(username)
//            .claim("role", role)
//            .setIssuedAt(new Date())
//            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))  // 15 minutes
//            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
//            .compact();
//    }
//
//    public String generateRefreshToken(String username, String role) {
//        return Jwts.builder()
//            .setSubject(username)
//            .claim("role", role)
//            .setIssuedAt(new Date())
//            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 24 hours
//            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
//            .compact();
//    }
//
//    public String extractUsername(String token) {
//        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey.getBytes());
//        Claims claims = jwtParser.parseClaimsJws(token).getBody();
//        return claims.getSubject();
//    }
//
//    public boolean isTokenExpired(String token) {
//        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey.getBytes());
//        Date expiration = jwtParser.parseClaimsJws(token).getBody().getExpiration();
//        return expiration.before(new Date());
//    }
//
//    public boolean validateToken(String token, String username) {
//        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
//    }
//
//    public String extractRole(String token) {
//        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey.getBytes());
//        Claims claims = jwtParser.parseClaimsJws(token).getBody();
//        return claims.get("role", String.class);
//    }
//}



package com.example.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey = "secret123456778901234567891234567890"; // Move to config for prod

    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))  // 2 minutes
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
            .compact();
    }

    public String generateRefreshToken(String username, String role) {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000))  // 1 hours
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
            .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey.getBytes())
            .parseClaimsJws(token)
            .getBody();
    }
}
