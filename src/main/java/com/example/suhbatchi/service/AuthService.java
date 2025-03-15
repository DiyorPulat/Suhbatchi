package com.example.suhbatchi.service;

import com.example.suhbatchi.caller.OtpCaller;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.*;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.repostory.UserRepostory;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepostory userRepostory;
    private final OtpCaller otpCaller;
    private final OtpService otpService;

    public AuthService(UserRepostory userRepostory, OtpCaller otpCaller, OtpService otpService) {
        this.userRepostory = userRepostory;
        this.otpCaller = otpCaller;
        this.otpService = otpService;
    }


    public Boolean checkUserExists(String phoneNumber) {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        return user.isPresent();
    }

    public User getClientIdByPhoneNumber(String phoneNumber) {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);

        return user.get();
    }


    public Cookie createCokkie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }


    public String saveName(NameRequest nameRequest, String phoneNumber) {
        User user = new User();
        user.setFirstName(nameRequest.name());
        user.setLastName(nameRequest.lastname());
        user.setPhoneNumber(phoneNumber);
        User userEntity = userRepostory.save(user);
        return userEntity.getClientId();
    }

    public String makePasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public void savePassword(PasswordRequest passwordRequest, String client_id) throws NoSuchAlgorithmException {
        User user = userRepostory.findByClientId(client_id);
        user.setPasswordHash(makePasswordHash(passwordRequest.password()));
        userRepostory.save(user);
    }


    public String registerClient(String clientId){
        User user = userRepostory.findByClientId(clientId);
        return otpService.makeMessage(user.getPhoneNumber(),1);
    }


}
