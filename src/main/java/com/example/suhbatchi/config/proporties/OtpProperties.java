package com.example.suhbatchi.config.proporties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "otp")
public record OtpProperties(
        String baseUrl
) {
}
