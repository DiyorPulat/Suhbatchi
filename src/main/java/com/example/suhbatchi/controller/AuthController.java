package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.NameRequest;
import com.example.suhbatchi.dto.PasswordRequest;
import com.example.suhbatchi.dto.PhoneNumberRequest;
import com.example.suhbatchi.dto.VerifyRequest;
import com.example.suhbatchi.dto.response.ClientInfoResponse;
import com.example.suhbatchi.dto.response.OtpIdResponse;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.OtpService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
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

            return new ResponseEntity<>(new ClientInfoResponse(user.getClientId()), HttpStatusCode.valueOf(200));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(ProjectConstants.SAVE_NAMES)
    public ResponseEntity<?> saveNames(@RequestBody NameRequest nameRequest) {
        if (nameRequest.phoneNumber().isEmpty()) {
            return ResponseEntity.badRequest().body("PhoneNumber is missing");
        }
        String clientId = authService.saveName(nameRequest, nameRequest.phoneNumber());
        return new ResponseEntity<>(new ClientInfoResponse(clientId), HttpStatusCode.valueOf(200));

    }

    @PostMapping(ProjectConstants.SAVE_PHONE)
    public ResponseEntity<?> savePhone(@RequestBody PasswordRequest passwordRequest) throws NoSuchAlgorithmException {
        if (passwordRequest.clientId().isEmpty()) {
            return ResponseEntity.badRequest().body("Client id  is missing");
        }
        authService.savePassword(passwordRequest, passwordRequest.clientId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody ClientInfoResponse clientInfoResponse) {
        if (clientInfoResponse.getClientId().isEmpty()) {
            return ResponseEntity.badRequest().body("Client id  is missing");
        }
        String id = authService.registerClient(clientInfoResponse.getClientId());
        return new ResponseEntity<>(new OtpIdResponse(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest) {
        if (otpService.verifyOtpCode(verifyRequest)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


}
