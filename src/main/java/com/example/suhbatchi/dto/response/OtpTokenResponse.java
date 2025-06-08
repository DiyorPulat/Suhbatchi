package com.example.suhbatchi.dto.response;

public record OtpTokenResponse(String message, Data data, String tokenType) {

    public record Data(String token) {}

}
