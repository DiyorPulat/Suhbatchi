package com.example.suhbatchi.config;

import com.example.suhbatchi.config.proporties.TaxProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class TaxWebClientConfig {

    @Bean(name = "taxWebClient")
    public WebClient taxWebClient(@Autowired TaxProperties taxProperties) {
        return WebClient.builder().baseUrl(taxProperties.baseUrl()).build();
    }
}
