//package com.example.controller;
//
//import com.example.model.AuthRequest;
//import com.example.model.User;
//import com.example.service.UserService;
//import com.example.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//    	System.out.println("hiii");
//        if (user.getUsername().equals("admin")) {
//            user.setRole("ROLE_ADMIN");
//        } else {
//            user.setRole("ROLE_USER");
//        }
//        userService.save(user);
//        return "User registered successfully!";
//    }
//
//    // Login & generate token
//    @GetMapping("/login")
//    public String login(@RequestBody AuthRequest authRequest) {
////        try {
////            // Authenticate the user
////            authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
////            );
////        } catch (AuthenticationException e) {
////            return "Invalid username/password";
////        }
//
//        // Fetch user role from the database (userService)
//    	String name=authRequest.getUsername();
//    	System.out.println(name);
//        String role = userService.getRoleByUsername(authRequest.getUsername());
//        System.out.println(role);
//
//        // Generate the access token with username and role
//        return jwtUtil.generateAccessToken(authRequest.getUsername(), role);
//    }
//}


//package com.example.controller;
//
//import com.example.model.AuthRequest;
//import com.example.model.User;
//import com.example.service.UserService;
//import com.example.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//        if ("admin".equalsIgnoreCase(user.getUsername())) {
//            user.setRole("ROLE_ADMIN");
//        } else {
//            user.setRole("ROLE_USER");
//        }
//        userService.save(user);
//        return "User registered successfully!";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody AuthRequest authRequest) {
//        try {
//            authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
//            );
//        } catch (AuthenticationException e) {
//            return "Invalid username or password";
//        }
//
//        String role = userService.getRoleByUsername(authRequest.getUsername());
//        return jwtUtil.generateAccessToken(authRequest.getUsername(), role);
//    }
//}












package com.example.controller;

import com.example.model.AuthRequest;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import com.mysql.cj.protocol.AuthenticationProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    private ProviderManager authenticationp;
    
   
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
    	 log.info("Something happened!"+user.getUsername());
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }
        userService.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        log.info("Admin is authenticated!"+authentication);
 
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByUsername(authRequest.getUsername());

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole());

        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // Use HTTPS in production
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(refreshTokenCookie);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);


        return ResponseEntity.ok(tokens);
    }
    

}


