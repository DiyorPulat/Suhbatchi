package com.example.suhbatchi.exception;

public class PasswordInCorrectException extends RuntimeException{
    public PasswordInCorrectException(String message) {
        super(message);
    }
}
