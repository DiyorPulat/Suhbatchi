package com.example.suhbatchi.controller;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.NameRequest;
import com.example.suhbatchi.dto.request.PasswordRequest;
import com.example.suhbatchi.dto.request.PhoneNumberRequest;
import com.example.suhbatchi.dto.request.VerifyRequest;
import com.example.suhbatchi.dto.response.ClientInfoResponse;
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
    @Operation(
            summary = "Auth token kerak emas",
            description = "Telefon raqamni tekshiradi va vaqtinchalik token yaratadi. Foydalanuvchi mavjudligini bildiradi.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Foydalanuvchi mavjud va token yaratildi",
                            content = @Content(schema = @Schema(implementation = ClientInfoResponse.UserExistResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Foydalanuvchi mavjud emas, ammo token yaratildi",
                            content = @Content(schema = @Schema(implementation = ClientInfoResponse.UserExistResponse.class))
                    )
            }
    )
    @PostMapping(ProjectConstants.GET_PHONE)
    public ResponseEntity<?> getNumber(@RequestBody PhoneNumberRequest phoneNumberRequest) {
        authService.isValidPhoneNumber(phoneNumberRequest.phoneNumber());
        String tempToken = jwtUtils.generateTemporaryToken(phoneNumberRequest.phoneNumber());
        if (authService.checkUserExists(phoneNumberRequest.phoneNumber())) {
            return ResponseEntity.ok().body(new ClientInfoResponse.UserExistResponse(true, tempToken));
        }
        return ResponseEntity.badRequest().body(new ClientInfoResponse.UserExistResponse(false, tempToken));
    }

    @Operation(
            summary = "Auth token kerak",
            description = "Foydalanuvchining ismlarini saqlash uchun API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ismlar muvaffaqiyatli saqlandi"
                    )
            }
    )
    @PostMapping(ProjectConstants.SAVE_NAMES)
    public ResponseEntity<?> saveNames(@RequestBody NameRequest nameRequest, HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        authService.saveName(nameRequest, phoneNumber);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Auth token kerak",
            description = "Parolni saqlaydi va foydalanuvchini ro'yxatdan o'tkazadi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parol saqlandi va foydalanuvchi ro'yxatdan o'tkazildi"
                    )
            }
    )
    @PostMapping(ProjectConstants.SAVE_PHONE)
    public ResponseEntity<?> savePhone(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) throws NoSuchAlgorithmException {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        authService.savePassword(passwordRequest, phoneNumber);
        authService.registerClient(phoneNumber);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Auth token kerak",
            description = "Telefon raqamga qayta SMS yuboradi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SMS muvaffaqiyatli yuborildi"
                    )
            }
    )
    @GetMapping("/resend-message")
    public ResponseEntity<?> sendMessage(HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        authService.registerClient(phoneNumber);
        return ResponseEntity.ok().build();

    }

    @Operation(
            summary = "Auth token kerak",
            description = "OTP kodni tekshiradi va permanent token yaratadi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OTP kod to‘g‘ri, doimiy token yaratildi",
                            content = @Content(schema = @Schema(implementation = Map.class))
                    )
            }
    )
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest, HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        otpService.verifyOtpCode(verifyRequest, phoneNumber);
        Map<String, String> map = authService.createPermanentToken(phoneNumber);
        return ResponseEntity.ok().body(map);
    }


}
