//package com.example.controller;
//
//import com.example.service.UserService;
//import com.example.util.JwtUtil;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//public class TokenController {
//
//    private final JwtUtil jwtUtil;
//    
//    @Autowired
//    UserService userService;
//
//    public TokenController(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @GetMapping("/refresh")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
//        String refreshToken = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("refresh_token")) {
//                    refreshToken = cookie.getValue();
//                }
//            }
//        }
//        
//        if (refreshToken != null && !jwtUtil.isTokenExpired(refreshToken)) {
//            String username = jwtUtil.extractUsername(refreshToken);
//            String role = userService.getRoleByUsername(username); // ✅ Get user role
//            String newAccessToken = jwtUtil.generateAccessToken(username, role);
//            return ResponseEntity.ok(newAccessToken);
//        } else {
//            return ResponseEntity.status(403).body("Refresh token is invalid or expired.");
//        }
//
//    }
//        
//}




package com.example.controller;

import com.example.service.UserService;
import com.example.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController {

    private final JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    public TokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null && !jwtUtil.isTokenExpired(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            String role = userService.getRoleByUsername(username);

            String newAccessToken = jwtUtil.generateAccessToken(username, role);

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("access_token", newAccessToken);
            tokenMap.put("refresh_token", refreshToken);

            return ResponseEntity.ok(tokenMap);
        } else {
            return ResponseEntity.status(403).body("Refresh token is invalid or expired.");
        }
    }
}

