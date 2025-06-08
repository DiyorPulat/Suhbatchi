package com.example.suhbatchi.dto.request;

import lombok.Getter;
import org.springframework.stereotype.Service;


@Service
@Getter
public class AuthenticationRequest {
    private String phoneNumber;
    private String password;
}
