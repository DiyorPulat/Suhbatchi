package com.example.suhbatchi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VerifyRequest(
        @JsonProperty("otpId")
        String otpId,
        @JsonProperty("otpCode")
        String code
) {
}
