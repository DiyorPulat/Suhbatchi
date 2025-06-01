package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.*;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    // ─────────────────────────── AUTH APIs ─────────────────────────── //

    @Operation(
            summary = "Telefon raqami roʻyxatdan o‘tgan‑o‘tmaganini tekshirish",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Bu telefon raqamdan ro‘yhatdan o‘tgan",
                    content = @Content(schema = @Schema(implementation = UserExistResponse.class))
            )
    )
    @PostMapping(ProjectConstants.GET_PHONE)
    public ResponseEntity<?> getNumber(@RequestBody PhoneNumberRequest phoneNumberRequest) {
        if (authService.isValidPhoneNumber(phoneNumberRequest.phoneNumber())) {
            String tempToken = jwtUtils.generateTemporaryToken(phoneNumberRequest.phoneNumber());
            boolean exists = authService.checkUserExists(phoneNumberRequest.phoneNumber());
            return exists
                    ? ResponseEntity.ok(new UserExistResponse(true, tempToken))
                    : ResponseEntity.badRequest().body(new UserExistResponse(false, tempToken));
        }
        return ResponseEntity.badRequest()
                .body("Invalid phone number format. It must start with 998 and have 9 digits.");
    }

    @Operation(
            summary = "Ism‑familiyani saqlash",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Muvaffaqiyatli saqlandi"
            )
    )
    @PostMapping(ProjectConstants.SAVE_NAMES)
    public ResponseEntity<?> saveNames(@RequestBody NameRequest nameRequest,
                                       HttpServletRequest request) {

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

    @Operation(
            summary = "Parolni saqlash va foydalanuvchini ro‘yxatdan o‘tkazish",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Muvaffaqiyatli parol saqlandi va ro‘yxatga olindi"
            )
    )
    @PostMapping(ProjectConstants.SAVE_PHONE)
    public ResponseEntity<?> savePhone(@RequestBody PasswordRequest passwordRequest,
                                       HttpServletRequest request)
            throws NoSuchAlgorithmException {

        String authHeader = request.getHeader("Authorization");
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

    @Operation(
            summary = "SMS kodni qayta yuborish",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Kod yuborildi"
            )
    )
    @GetMapping("/resend-message")
    public ResponseEntity<?> sendMessage(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authService.isValidToken(authHeader)) {
            log.info("auth : {}", authHeader);
            String token = authHeader.substring(7);
            String phoneNumber = jwtUtils.extractUsername(token);
            log.info("phone number : {}", phoneNumber);
            if (phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("PhoneNumber in token is missing");
            }
            authService.registerClient(phoneNumber);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Token is invalid or expired");
    }

    @Operation(
            summary = "OTP kodni tekshirish va doimiy token yaratish",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Muvaffaqiyatli tasdiqlandi",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    )
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest,
                                       HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
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
