package com.example.suhbatchi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OtpRequest(
        @JsonProperty("mobile_phone")
        String mobilePhone,
        @JsonProperty("message")
        String message,
        @JsonProperty("from")
        String from
) {
}
