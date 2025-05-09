package com.example.suhbatchi.caller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.companyDtos.CompanyResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

@Component
@Slf4j
public class TaxCaller {
    private final WebClient webClient;

    @Autowired
    public TaxCaller(@Qualifier("taxWebClient") WebClient webClient) {
        this.webClient = webClient;
    }


    public CompanyResponseDTO getTaxInfo(String INN) {
        String x_API_KEY = "546c3a8b-304e-408a-85b8-788c11c40341";
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(ProjectConstants.TAX_INFO_URL)
                        .queryParam("type", ProjectConstants.TYPE_OF_FULL)
                        .build(INN))
                .header("X-API-KEY", x_API_KEY)
                .retrieve()
                .bodyToMono(CompanyResponseDTO.class)
                .doOnNext(response -> {
                    log.info("✅ URL called: {}", "https://your-base-url" + ProjectConstants.TAX_INFO_URL.replace("{INN}", INN) + "?type=" + ProjectConstants.TYPE_OF_FULL);
                    log.info("✅ Raw response: {}", response);
                })
                .onErrorResume(ex -> {
                    if (ex instanceof ConnectException ||
                            ex instanceof SocketTimeoutException ||
                            ex instanceof WebClientResponseException) {
                        return Mono.just(new CompanyResponseDTO());
                    }
                    return Mono.error(ex);
                })
                .block();
    }
}
