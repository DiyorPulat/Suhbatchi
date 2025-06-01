package com.example.suhbatchi.service;

import com.example.suhbatchi.caller.OtpCaller;
import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.dto.NameRequest;
import com.example.suhbatchi.dto.PasswordRequest;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.repostory.UserRepostory;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthService {
    private final UserRepostory userRepostory;
    private final OtpCaller otpCaller;
    private final OtpService otpService;
    private final JwtUtils jwtUtils;


    private static final Pattern PHONE_PATTERN = Pattern.compile("^998\\d{9}$");

    public boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }


    public Boolean isValidToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtils.isTokenExpired(token);
        }
        return false;
    }


    public AuthService(UserRepostory userRepostory, OtpCaller otpCaller, OtpService otpService, JwtUtils jwtUtils) {
        this.userRepostory = userRepostory;
        this.otpCaller = otpCaller;
        this.otpService = otpService;
        this.jwtUtils = jwtUtils;
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

    public void savePassword(PasswordRequest passwordRequest, String phoneNumber) throws NoSuchAlgorithmException {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            User userEntity = user.get();
            userEntity.setPasswordHash(makePasswordHash(passwordRequest.password()));
            userRepostory.save(userEntity);
        } else {
            log.error("savePassword User not found {}", phoneNumber);
        }
    }


    public void registerClient(String phoneNumber) {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        user.ifPresent(value -> otpService.makeMessage(value.getPhoneNumber(), 1));
    }


    public Map<String, String> createPermanentToken(String phoneNumber) {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        Map<String, String> map = new HashMap<>();
        if (user.isPresent()) {
            String permenantToken = jwtUtils.generatePermanentToken(user.get().getClientId());
            String refreshToken = jwtUtils.refreshToken(user.get().getClientId());
            map.put("permenantToken", permenantToken);
            map.put("refreshToken", refreshToken);
        }
        return map;
    }
}
