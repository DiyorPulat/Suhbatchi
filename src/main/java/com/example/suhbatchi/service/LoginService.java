package com.example.suhbatchi.service;

import com.example.suhbatchi.dto.PhoneNumberRequest;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.repostory.UserRepostory;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class LoginService {
    private final AuthService authService;
    private final UserRepostory userRepostory;
    private final OtpService otpService;

    public LoginService(AuthService authService, UserRepostory userRepostory, OtpService otpService) {
        this.authService = authService;

        this.userRepostory = userRepostory;
        this.otpService = otpService;
    }


    public Boolean checkPassword(String client, String password) throws NoSuchAlgorithmException {
        User user =  userRepostory.findByClientId(client);
        String passwordInput = user.getPasswordHash();
        String passwordOutput = authService.makePasswordHash(password);
        return passwordInput.equals(passwordOutput);
    }

    public String sendOtpForUpdatePassword(PhoneNumberRequest phoneNumberRequest) throws NoSuchAlgorithmException {
        return otpService.makeMessage(phoneNumberRequest.phoneNumber(), 2);
    }

    public void updatePassword(String client, String password) throws NoSuchAlgorithmException {
        User user =  userRepostory.findByClientId(client);
        String passwordInput = authService.makePasswordHash(password);
        user.setPasswordHash(passwordInput);
        userRepostory.save(user);
    }
}
