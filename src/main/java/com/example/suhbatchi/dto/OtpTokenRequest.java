package com.example.suhbatchi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OtpTokenRequest(
        @JsonProperty("email")
        String email,
        @JsonProperty("password")
        String password
) {
}
