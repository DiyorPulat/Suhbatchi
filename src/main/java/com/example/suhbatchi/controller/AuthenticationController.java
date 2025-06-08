package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.dto.request.AuthenticationRequest;
import com.example.suhbatchi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

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

    public AuthenticationController(UserDetailsService userDetailsService,
                                    JwtUtils jwtUtils,
                                    AuthenticationManager authenticationManager,
                                    AuthService authService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @Operation(
            summary = "Telefon raqami va parol orqali autentifikatsiya",
            description = "Raqam va parol to‘g‘ri bo‘lsa, foydalanuvchiga doimiy JWT token qaytaradi.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli autentifikatsiya",
                            content = @Content(schema = @Schema(implementation = Map.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Noto‘g‘ri telefon raqami",
                            content = @Content(schema = @Schema(implementation = Map.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Noto‘g‘ri raqam yoki parol",
                            content = @Content(schema = @Schema(implementation = Map.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Foydalanuvchi topilmadi",
                            content = @Content(schema = @Schema(implementation = Map.class))
                    )
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request)
            throws NoSuchAlgorithmException {

        log.info("Authenticating request: {}", request.getPhoneNumber());

        authService.isValidPhoneNumber(request.getPhoneNumber());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getPhoneNumber(),
                            authService.makePasswordHash(request.getPassword()))
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid phone number or password"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhoneNumber());
        if (userDetails == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        Map<String, String> mapResponse = authService.createPermanentToken(request.getPhoneNumber());
        return ResponseEntity.ok(mapResponse);
    }
}
