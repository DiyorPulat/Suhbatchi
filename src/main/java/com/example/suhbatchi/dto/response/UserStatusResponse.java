package com.example.suhbatchi.dto.response;

import lombok.Data;

@Data
public class UserStatusResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean IsActiveUser;
}
