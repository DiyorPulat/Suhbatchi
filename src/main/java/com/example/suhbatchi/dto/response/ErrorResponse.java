package com.example.suhbatchi.dto.response;

public class ErrorResponse {
    Integer code;
    String message;

    public ErrorResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public static ErrorResponse getErrorResponse(Integer code, String message) {
        return new ErrorResponse(code, message);
    }
}
