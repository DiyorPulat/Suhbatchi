package com.example.suhbatchi.dto;

import lombok.Getter;
import org.springframework.stereotype.Service;


@Service
@Getter
public class AuthenticationRequest {
    private String phoneNumber;
    private String password;
}
