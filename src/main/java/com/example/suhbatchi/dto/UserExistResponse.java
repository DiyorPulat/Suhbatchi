package com.example.suhbatchi.dto;

public record UserExistResponse(
        Boolean userExist,
        String token
) {
}
