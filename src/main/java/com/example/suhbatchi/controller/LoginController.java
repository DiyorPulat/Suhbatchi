package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.PasswordRequest;
import com.example.suhbatchi.dto.PhoneNumberRequest;
import com.example.suhbatchi.dto.VerifyRequest;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.LoginService;
import com.example.suhbatchi.service.OtpService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordRequest passwordRequest) throws NoSuchAlgorithmException {
        if (passwordRequest == null || passwordRequest.clientId().isEmpty()) {
            return ResponseEntity.badRequest().body("clientId is null or empty");
        }

        if (loginService.checkPassword(passwordRequest.clientId(), passwordRequest.password())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody PhoneNumberRequest phoneNumberRequest) throws NoSuchAlgorithmException {
        String otpId = loginService.sendOtpForUpdatePassword(phoneNumberRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/veriy-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest) throws NoSuchAlgorithmException {
        if (otpService.verifyOtpCode(verifyRequest)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest passwordRequest,@CookieValue(value = "clientId") String clientId, HttpServletResponse response) throws NoSuchAlgorithmException {
        loginService.updatePassword(clientId,passwordRequest.password());
        return ResponseEntity.ok().build();
    }
}
