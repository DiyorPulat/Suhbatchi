package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.*;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.OtpService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(ProjectConstants.AUTH)
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping(ProjectConstants.GET_PHONE)
    public ResponseEntity<?> getNumber(@RequestBody PhoneNumberRequest phoneNumberRequest, HttpServletResponse response) {
        if (authService.checkUserExists(phoneNumberRequest.phoneNumber())) {
            User user = authService.getClientIdByPhoneNumber(phoneNumberRequest.phoneNumber());
            response.addCookie(authService.createCokkie("clientId", user.getClientId()));
            return ResponseEntity.ok().build();
        } else {
            response.addCookie(authService.createCokkie("phoneNumber", phoneNumberRequest.phoneNumber()));
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(ProjectConstants.SAVE_NAMES)
    public ResponseEntity<?> saveNames(@RequestBody NameRequest nameRequest, @CookieValue(value = "phoneNumber") String phoneNumber, HttpServletResponse response) {
        if (phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("PhoneNumber cookie is missing");
        }
        String clientId = authService.saveName(nameRequest, phoneNumber);
        response.addCookie(authService.createCokkie("clientId", clientId));
        response.addCookie(authService.createCokkie("phoneNumber", null));
        return ResponseEntity.ok().build();
    }

    @PostMapping(ProjectConstants.SAVE_PHONE)
    public ResponseEntity<?> savePhone(@RequestBody PasswordRequest passwordRequest, @CookieValue(value = "clientId") String clientId, HttpServletResponse response) throws NoSuchAlgorithmException {
        if (clientId.isEmpty()) {
            return ResponseEntity.badRequest().body("Client id cookie is missing");
        }
        authService.savePassword(passwordRequest, clientId);
        return ResponseEntity.ok().build();

    }


    @GetMapping("/send-message")
    public ResponseEntity<?> sendMessage(@CookieValue(value = "clientId") String clientId, HttpServletResponse response) {
        if (clientId.isEmpty()) {
            return ResponseEntity.badRequest().body("Client id cookie is missing");
        }
        String id = authService.registerClient(clientId);
        response.addCookie(authService.createCokkie("otpId", id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest, @CookieValue(value = "otpId") String id, HttpServletResponse response) {
        if (otpService.verifyOtpCode(id, verifyRequest)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


}
