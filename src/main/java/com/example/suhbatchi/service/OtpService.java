package com.example.suhbatchi.service;

import com.example.suhbatchi.caller.OtpCaller;
import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.OtpRequest;
import com.example.suhbatchi.dto.request.OtpTokenRequest;
import com.example.suhbatchi.dto.request.VerifyRequest;
import com.example.suhbatchi.dto.response.OtpResponse;
import com.example.suhbatchi.dto.response.OtpTokenResponse;
import com.example.suhbatchi.entity.OtpTemplate;
import com.example.suhbatchi.exception.OtpCodeInvalidException;
import com.example.suhbatchi.repostory.OtpTemplateRepostory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class OtpService {
    private final OtpCaller otpCaller;
    private final OtpTemplateRepostory otpTemplateRepostory;

    public OtpService(OtpCaller otpCaller, OtpTemplateRepostory otpTemplateRepostory) {
        this.otpCaller = otpCaller;
        this.otpTemplateRepostory = otpTemplateRepostory;
    }

    public OtpTokenResponse getToken() {
        return otpCaller.getToken(new OtpTokenRequest(ProjectConstants.EMAIL_ESKIZ, ProjectConstants.PASSWORD_ESKIZ));
    }


    public OtpResponse sendMessage(OtpRequest otpRequest) {
        OtpTokenResponse otpResponse = getToken();
        System.out.println(otpResponse);
        System.out.println(otpRequest);
        return otpCaller.sendMessage(otpRequest, otpResponse.data().token());
    }


    public String generateOtpCode() {
        Random random = new Random();  // Create a Random object
        StringBuilder combinedNumbers = new StringBuilder();  // StringBuilder to store the combined numbers

        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(10);  // Generate random number between 0 and 9
            combinedNumbers.append(randomNumber);  // Append the number to the StringBuilder
        }

        return combinedNumbers.toString();
    }


    public String makeMessage(String phone, Integer messageId) {
        String otpCode = generateOtpCode();
        OtpTemplate otpTemplate = new OtpTemplate();
        if (messageId == 1) {
            OtpResponse otpResponse = sendMessage(new OtpRequest(phone, ProjectConstants.OTP_MESSAGE_1 + otpCode, ProjectConstants.DEFAULT_FROM));
            otpTemplate.setOtpStatus(otpResponse.status());
        }
        if (messageId == 2) {
            OtpResponse otpResponse = sendMessage(new OtpRequest(phone, ProjectConstants.OTP_MESSAGE_2 + otpCode, ProjectConstants.DEFAULT_FROM));
            otpTemplate.setOtpStatus(otpResponse.status());
        }
        if (otpTemplate.getOtpStatus() != null) {
            otpTemplate.setPhoneNumber(phone);
            otpTemplate.setMessageId(messageId);
            otpTemplate.setOtpCode(otpCode);

        }
        log.info("otp code: {}", otpCode);
        otpTemplate.setCreatedAt(LocalDateTime.now());
        OtpTemplate otpTemplate1 = otpTemplateRepostory.save(otpTemplate);

        return otpTemplate1.getId().toString();
    }


    public void verifyOtpCode(VerifyRequest verifyRequest, String phoneNumber) {
        OtpTemplate otpTemplate = otpTemplateRepostory.findOtpTemplateByPhoneNumber(phoneNumber);
        if (!otpTemplate.getOtpCode().trim().equals(verifyRequest.code().trim())){
            throw new OtpCodeInvalidException("OTP code is incorrect");
        }
    }
}
