package com.example.suhbatchi.config;

import com.example.suhbatchi.config.proporties.OtpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class OtpWebClientConfig {


    @Bean(name = "otpWebClient")
    public WebClient OtpWebClient(@Autowired OtpProperties otpProperties) {
        return WebClient.builder().baseUrl(otpProperties.baseUrl()).build();
    }
}
