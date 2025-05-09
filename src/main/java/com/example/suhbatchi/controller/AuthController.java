package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.*;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.OtpService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping(ProjectConstants.AUTH)
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, OtpService otpService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.otpService = otpService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(ProjectConstants.GET_PHONE)
    public ResponseEntity<?> getNumber(@RequestBody PhoneNumberRequest phoneNumberRequest) {
        if (authService.isValidPhoneNumber(phoneNumberRequest.phoneNumber())) {
            String tempToken = jwtUtils.generateTemporaryToken(phoneNumberRequest.phoneNumber());
            if (authService.checkUserExists(phoneNumberRequest.phoneNumber())) {
                return ResponseEntity.ok().body(new UserExistResponse(true, tempToken));
            }
            return ResponseEntity.badRequest().body(new UserExistResponse(false, tempToken));
        }
        return ResponseEntity.badRequest().body("Invalid phone number format. It must start with 998 and have 9 digits.");
    }

    @PostMapping(ProjectConstants.SAVE_NAMES)
    public ResponseEntity<?> saveNames(@RequestBody NameRequest nameRequest, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authService.isValidToken(authHeader)) {
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            authService.saveName(nameRequest, phoneNumber);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @PostMapping(ProjectConstants.SAVE_PHONE)
    public ResponseEntity<?> savePhone(@RequestBody PasswordRequest passwordRequest, @RequestHeader("Authorization") String authHeader) throws NoSuchAlgorithmException {
        if (authService.isValidToken(authHeader)) {
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            authService.savePassword(passwordRequest, phoneNumber);
            authService.registerClient(phoneNumber);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @GetMapping("/resend-message")
    public ResponseEntity<?> sendMessage(@RequestHeader("Authorization") String authHeader) {
        if (authService.isValidToken(authHeader)) {
            log.info("auth : {}", authHeader);
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            log.info("phone number : {}", phoneNumber);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            log.info("phonenumeb : {}", phoneNumber);
            authService.registerClient(phoneNumber);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest, @RequestHeader("Authorization") String authHeader) {
        if (authService.isValidToken(authHeader)) {
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            if (otpService.verifyOtpCode(verifyRequest, phoneNumber)) {
                Map<String, String> map = authService.createPermanentToken(phoneNumber);
                return ResponseEntity.ok().body(map);
            }
            return ResponseEntity.badRequest().body("Incorrect otp code");
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }


}
