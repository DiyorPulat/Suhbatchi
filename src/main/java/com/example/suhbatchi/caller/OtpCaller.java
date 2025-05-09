package com.example.suhbatchi.caller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.OtpRequest;
import com.example.suhbatchi.dto.OtpResponse;
import com.example.suhbatchi.dto.OtpTokenRequest;
import com.example.suhbatchi.dto.OtpTokenResponse;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class OtpCaller {
    private final WebClient webClient;

    @Autowired
    public OtpCaller(@Qualifier("otpWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public OtpTokenResponse getToken(OtpTokenRequest otpTokenRequest) {
        return webClient.post()
                .uri(ProjectConstants.GET_TOKEN_FROM_OTP)
                .bodyValue(otpTokenRequest)
                .exchangeToMono(res -> {
                            if (res.statusCode().is2xxSuccessful()) {
                                return res.bodyToMono(OtpTokenResponse.class);
                            } else {

                                return res
                                        .bodyToMono(String.class)
                                        .map(data -> new OtpTokenResponse(null, null, null));
                            }
                        }

                )
                .onErrorResume(ConnectTimeoutException.class, ex -> {

                    return Mono.just(new OtpTokenResponse(null, null, null));
                })
                .onErrorResume(ReadTimeoutException.class, ex -> {

                    return Mono.just(new OtpTokenResponse(null, null, null));
                })
                .onErrorResume(WebClientException.class, ex -> {

                    return Mono.just(new OtpTokenResponse(null, null, null));
                })
                .block();
    }

    public OtpResponse sendMessage(OtpRequest otpRequest, String token) {
        return webClient.post()
                .uri(ProjectConstants.SEND_OTP_URL)
                .header("Authorization", "Bearer " + token)
                .bodyValue(otpRequest)
                .exchangeToMono(res -> {
                    if (res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(OtpResponse.class);
                    } else {
                        // If the response is not successful, throw an exception with the error details
                        return res.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "Error occurred: Status Code - " + res.statusCode() + ", Response Body: " + errorBody)));
                    }
                })
                .block();
    }


}
