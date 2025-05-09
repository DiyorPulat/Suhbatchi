package com.example.suhbatchi.config.proporties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tax")
public record TaxProperties(String baseUrl) {
}
