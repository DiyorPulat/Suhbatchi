package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.dto.AuthenticationRequest;
import com.example.suhbatchi.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthenticationController(UserDetailsService userDetailsService, JwtUtils jwtUtils, AuthenticationManager authenticationManager, AuthService authService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) throws NoSuchAlgorithmException {
        log.info("Authenticating request: {}", request.getPhoneNumber());
        if (authService.isValidPhoneNumber(request.getPhoneNumber())) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), authService.makePasswordHash(request.getPassword()))
                );
            } catch (Exception e) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid phone number or password"));
            }

            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhoneNumber());
            if (userDetails == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            Map<String, String> mapResponse = authService.createPermanentToken(request.getPhoneNumber());
            return ResponseEntity.ok(mapResponse);
        }
        return ResponseEntity.status(400).body(Map.of("error", "Invalid phone number"));

    }


}
