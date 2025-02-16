package com.example.suhbatchi.dto;

public record OtpTokenResponse(String message, Data data, String tokenType) {

    public record Data(String token) {}

}
