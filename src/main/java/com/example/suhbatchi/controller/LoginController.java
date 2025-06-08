package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.PasswordRequest;
import com.example.suhbatchi.dto.request.PhoneNumberRequest;
import com.example.suhbatchi.dto.request.VerifyRequest;
import com.example.suhbatchi.exception.PhoneMismatchException;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.LoginService;
import com.example.suhbatchi.service.OtpService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) throws NoSuchAlgorithmException {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        loginService.checkPassword(phoneNumber, passwordRequest.password());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> sendOtp(@RequestBody PhoneNumberRequest phoneNumberRequest, HttpServletRequest request) throws NoSuchAlgorithmException {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        authService.checkPhoneNumbers(phoneNumber, phoneNumberRequest.phoneNumber());
        loginService.sendOtpForUpdatePassword(phoneNumberRequest.phoneNumber());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/veriy-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest, HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        otpService.verifyOtpCode(verifyRequest, phoneNumber);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) throws NoSuchAlgorithmException {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        loginService.updatePassword(phoneNumber, passwordRequest.password());
        return ResponseEntity.ok().build();
    }
}


