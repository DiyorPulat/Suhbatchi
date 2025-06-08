package com.example.suhbatchi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VerifyRequest(
        @JsonProperty("otpCode")
        String code
) {
}
