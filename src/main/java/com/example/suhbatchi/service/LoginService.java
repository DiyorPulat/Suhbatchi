package com.example.suhbatchi.service;

import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.repostory.UserRepostory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class LoginService {
    private final AuthService authService;
    private final UserRepostory userRepostory;
    private final OtpService otpService;

    public LoginService(AuthService authService, UserRepostory userRepostory, OtpService otpService) {
        this.authService = authService;

        this.userRepostory = userRepostory;
        this.otpService = otpService;
    }


    public Boolean checkPassword(String phoneNumber, String password) throws NoSuchAlgorithmException {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            String passwordInput = user.get().getPasswordHash();
            String passwordOutput = authService.makePasswordHash(password);
            return passwordInput.equals(passwordOutput);
        }
        log.error("checkPassword user not found {}", phoneNumber);
        return false;
    }

    public String sendOtpForUpdatePassword(String phoneNumber) throws NoSuchAlgorithmException {
        return otpService.makeMessage(phoneNumber, 2);
    }

    public void updatePassword(String phoneNumber, String password) throws NoSuchAlgorithmException {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        String passwordInput = authService.makePasswordHash(password);
        User userEntity = user.get();
        userEntity.setPasswordHash(passwordInput);
        userRepostory.save(userEntity);
    }
}
