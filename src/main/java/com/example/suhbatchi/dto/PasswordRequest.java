package com.example.suhbatchi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PasswordRequest(
        @JsonProperty("password")
        String password
) {
}
