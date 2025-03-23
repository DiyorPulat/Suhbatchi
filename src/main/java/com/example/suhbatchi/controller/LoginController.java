package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.PasswordRequest;
import com.example.suhbatchi.dto.VerifyRequest;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.LoginService;
import com.example.suhbatchi.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(ProjectConstants.LOGIN)
public class LoginController {
    private final LoginService loginService;
    private final AuthService authService;
    private final OtpService otpService;
    private final JwtUtils jwtUtils;

    public LoginController(LoginService loginService, AuthService authService, OtpService otpService, JwtUtils jwtUtils) {
        this.loginService = loginService;
        this.authService = authService;
        this.otpService = otpService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordRequest passwordRequest, @RequestHeader("Authorization") String authHeader) throws NoSuchAlgorithmException {
        if (authService.isValidToken(authHeader)) {
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            if (loginService.checkPassword(phoneNumber, passwordRequest.password())) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body("Incorrect password");
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestHeader("Authorization") String authHeader) throws NoSuchAlgorithmException {
        if (authService.isValidToken(authHeader)) {
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            loginService.sendOtpForUpdatePassword(phoneNumber);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @PostMapping("/veriy-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest, @RequestHeader("Authorization") String authHeader) {
        if (authService.isValidToken(authHeader)) {
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            if (otpService.verifyOtpCode(verifyRequest, phoneNumber)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body("Incorrect otp code");
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest passwordRequest, @RequestHeader("Authorization") String authHeader) throws NoSuchAlgorithmException {
        if (authService.isValidToken(authHeader)) {
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            loginService.updatePassword(phoneNumber, passwordRequest.password());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

}
