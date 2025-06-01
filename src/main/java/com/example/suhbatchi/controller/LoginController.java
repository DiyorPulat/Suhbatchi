package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.PasswordRequest;
import com.example.suhbatchi.dto.PhoneNumberRequest;
import com.example.suhbatchi.dto.VerifyRequest;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.LoginService;
import com.example.suhbatchi.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    public LoginController(LoginService loginService,
                           AuthService authService,
                           OtpService otpService,
                           JwtUtils jwtUtils) {
        this.loginService = loginService;
        this.authService = authService;
        this.otpService = otpService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
            summary = "Kirish uchun parolni tekshirish",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Parol to‘g‘ri"
            )
    )
    @GetMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordRequest passwordRequest,
                                            HttpServletRequest request) throws NoSuchAlgorithmException {

        String authHeader = request.getHeader("Authorization");
        if (authService.isValidToken(authHeader)) {
            String phoneNumber = jwtUtils.extractUsername(authHeader.substring(7));
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            return loginService.checkPassword(phoneNumber, passwordRequest.password())
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.badRequest().body("Incorrect password");
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @Operation(
            summary = "Parolni yangilash uchun OTP kodini yuborish",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OTP yuborildi"
            )
    )
    @PostMapping("/resend-otp")
    public ResponseEntity<?> sendOtp(@RequestBody PhoneNumberRequest phoneNumberRequest,
                                     HttpServletRequest request) throws NoSuchAlgorithmException {

        String authHeader = request.getHeader("Authorization");
        if (authService.isValidToken(authHeader)) {
            String phoneNumber = jwtUtils.extractUsername(authHeader.substring(7));
            if (!phoneNumber.equals(phoneNumberRequest.phoneNumber())) {
                return ResponseEntity.badRequest()
                        .body("It seems the phone number you entered during registration doesn't match. Could you please double‑check and update it?");
            }
            loginService.sendOtpForUpdatePassword(phoneNumberRequest.phoneNumber());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }


    @Operation(
            summary = "Parolni yangilash uchun OTP kodini tekshirish",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OTP to‘g‘ri"
            )
    )
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest,
                                       HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authService.isValidToken(authHeader)) {
            String phoneNumber = jwtUtils.extractUsername(authHeader.substring(7));
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            return otpService.verifyOtpCode(verifyRequest, phoneNumber)
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.badRequest().body("Incorrect otp code");
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @Operation(
            summary = "Parolni yangilash",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Parol muvaffaqiyatli yangilandi"
            )
    )
    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest passwordRequest,
                                            HttpServletRequest request) throws NoSuchAlgorithmException {

        String authHeader = request.getHeader("Authorization");
        if (authService.isValidToken(authHeader)) {
            String phoneNumber = jwtUtils.extractUsername(authHeader.substring(7));
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            loginService.updatePassword(phoneNumber, passwordRequest.password());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }
}
