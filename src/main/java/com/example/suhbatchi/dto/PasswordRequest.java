package com.example.suhbatchi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PasswordRequest(
        @JsonProperty("clientId")
        String clientId,
        @JsonProperty("password")
        String password
) {
}
