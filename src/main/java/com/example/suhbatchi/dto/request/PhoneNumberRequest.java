package com.example.suhbatchi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhoneNumberRequest(
        @JsonProperty("phoneNumber")
        String phoneNumber
) {
}
