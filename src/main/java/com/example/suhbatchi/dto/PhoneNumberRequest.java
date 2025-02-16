package com.example.suhbatchi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhoneNumberRequest(
        @JsonProperty("phoneNumber")
        String phoneNumber
) {
}
