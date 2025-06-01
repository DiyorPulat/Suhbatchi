package com.example.suhbatchi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OtpIdResponse(
        @JsonProperty("otpId")
        String otpId
) {
}
