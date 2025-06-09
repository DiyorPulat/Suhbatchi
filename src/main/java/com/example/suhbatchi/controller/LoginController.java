package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.PasswordRequest;
import com.example.suhbatchi.dto.request.PhoneNumberRequest;
import com.example.suhbatchi.dto.request.VerifyRequest;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.LoginService;
import com.example.suhbatchi.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(ProjectConstants.LOGIN)
public class LoginController {
    private final LoginService loginService;
    private final AuthService authService;
    private final OtpService otpService;

    public LoginController(LoginService loginService, AuthService authService, OtpService otpService) {
        this.loginService = loginService;
        this.authService = authService;
        this.otpService = otpService;
    }
    @Operation(
            summary = "Auth token kerak",
            description = "Foydalanuvchining parolini tekshiradi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parol muvaffaqiyatli tekshirildi"
                    )
            }
    )
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request)  {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        loginService.checkPassword(phoneNumber, passwordRequest.password());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Auth token kerak",
            description = "Parolni yangilash uchun telefon raqamga OTP kod yuboradi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OTP kod muvaffaqiyatli yuborildi"
                    )
            }
    )

    @PostMapping("/resend-otp")
    public ResponseEntity<?> sendOtp(@RequestBody PhoneNumberRequest phoneNumberRequest, HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        authService.checkPhoneNumbers(phoneNumber, phoneNumberRequest.phoneNumber());
        loginService.sendOtpForUpdatePassword(phoneNumberRequest.phoneNumber());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Auth token kerak",
            description = "OTP kodni tekshiradi (parolni yangilash jarayonida)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OTP kod muvaffaqiyatli tekshirildi"
                    )
            }
    )
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest, HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        otpService.verifyOtpCode(verifyRequest, phoneNumber);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Auth token kerak",
            description = "Foydalanuvchining parolini yangilaydi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parol muvaffaqiyatli yangilandi"
                    )
            }
    )
    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) throws NoSuchAlgorithmException {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        loginService.updatePassword(phoneNumber, passwordRequest.password());
        return ResponseEntity.ok().build();
    }
}


