package com.example.suhbatchi.service;

import com.example.suhbatchi.config.JwtUtils;
import com.example.suhbatchi.dto.request.NameRequest;
import com.example.suhbatchi.dto.request.PasswordRequest;
import com.example.suhbatchi.entity.SessionModel;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.exception.InvalidFormatPhoneException;
import com.example.suhbatchi.exception.PhoneMismatchException;
import com.example.suhbatchi.exception.TokenException;
import com.example.suhbatchi.repostory.SessionRepository;
import com.example.suhbatchi.repostory.UserRepostory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthService {
    private final UserRepostory userRepostory;
    private final OtpService otpService;
    private final JwtUtils jwtUtils;
    private final SessionRepository sessionRepository;


    private static final Pattern PHONE_PATTERN = Pattern.compile("^998\\d{9}$");

    public void isValidPhoneNumber(String phoneNumber) {
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new InvalidFormatPhoneException("The phone number doesn't match our records. Please check and try again.");
        }
    }


    public void isValidToken(String authHeader) {
        log.info("auth : {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!jwtUtils.isTokenExpired(token)) {
                throw new TokenException("Token is expired");
            }
        } else {
            throw new TokenException("Invalid token");
        }
    }

    public String getPhoneNumberFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        isValidToken(authHeader);
        String token = authHeader.substring(7);
        String phoneNumber = jwtUtils.extractUsername(token);
        if (phoneNumber == null) {
            throw new TokenException("PhoneNumber in token is missing:" + phoneNumber);
        }
        return phoneNumber;
    }


    public AuthService(UserRepostory userRepostory, OtpService otpService, JwtUtils jwtUtils, SessionRepository sessionRepository) {
        this.userRepostory = userRepostory;
        this.otpService = otpService;
        this.jwtUtils = jwtUtils;
        this.sessionRepository = sessionRepository;
    }


    public Boolean checkUserExists(String phoneNumber) {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        return user.isPresent();
    }

    public void saveName(NameRequest nameRequest, String phoneNumber) {
        User user = new User();
        user.setFirstName(nameRequest.name());
        user.setLastName(nameRequest.lastname());
        user.setPhoneNumber(phoneNumber);
        user.setIsActiveUser(false);
        userRepostory.save(user);
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

    public void checkPhoneNumbers(String phoneNumber1, String phoneNumber2) {
        if (!phoneNumber1.equals(phoneNumber2)) {
            throw new PhoneMismatchException("The phone number doesn't match our records. Please check and try again.");
        }
    }


    public Map<String, String> createPermanentToken(String phoneNumber) {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        Map<String, String> map = new HashMap<>();
        if (user.isPresent()) {
            String permenantToken = jwtUtils.generatePermanentToken(user.get().getClientId());
            String refreshToken = jwtUtils.refreshToken(user.get().getClientId(),phoneNumber);
            map.put("permenantToken", permenantToken);
            map.put("refreshToken", refreshToken);
        }

        SessionModel sessionModel = SessionModel.of(phoneNumber,map.get("refreshToken"));
        sessionRepository.save(sessionModel);
        return map;
    }


    public Map<String, String> getToken(String refreshToken) {
        Map<String, String> tokens = new HashMap<>();

        if (!jwtUtils.isRefreshToken(refreshToken)) {
            throw new TokenException("Invalid refresh token");
        }

        String clientId = jwtUtils.extractUsername(refreshToken);
        if (clientId == null || clientId.isBlank()) {
            throw new TokenException("Client ID not found in refresh token");
        }

        User user = userRepostory.findByClientId(clientId);
        if (user == null) {
            throw new TokenException("Client not found");
        }

        SessionModel sessionModel = sessionRepository.getByPhoneNumber(user.getPhoneNumber());
        if (sessionModel == null || Boolean.TRUE.equals(sessionModel.getIsDeleted())) {
            throw new TokenException("Client session is deleted or not found");
        }

        if (jwtUtils.isTokenExpired(refreshToken)) {
            sessionModel.setIsDeleted(Boolean.TRUE);
            sessionRepository.save(sessionModel);
            throw new TokenException("Refresh token is expired");
        }

        checkUserExists(user.getPhoneNumber());

        String permanentToken = jwtUtils.generatePermanentToken(user.getClientId());
        tokens.put("permanentToken", permanentToken);

        return tokens;
    }



    public void logout(String phoneNumber) {
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            SessionModel sessionModel = sessionRepository.getByPhoneNumber(user.get().getPhoneNumber());
            sessionModel.setIsDeleted(Boolean.TRUE);
            sessionRepository.save(sessionModel);
        }
    }



}
