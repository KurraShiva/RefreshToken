
package com.example.controller;

import com.example.model.AuthRequest;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/refresh")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findByUsername(authRequest.getUsername());

            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole());

            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true); // ensure HTTPS in production
            refreshTokenCookie.setPath("/"); // cookie accessible for the whole domain
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration
            // SameSite attribute is not directly supported in Cookie, may need server config or response header workaround

            response.addCookie(refreshTokenCookie);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);

            return ResponseEntity.ok(tokens);
        } catch (Exception ex) {
            // Authentication failed
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}

