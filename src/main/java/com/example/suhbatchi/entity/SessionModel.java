package com.example.suhbatchi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SessionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sessionId;
    private String phoneNumber;
    @Column(length = 3000)
    private String refreshToken;
    private LocalDate createdDate;
    private LocalDate expiryDate;
    private Boolean isDeleted;


    public static SessionModel  of(String phoneNumber, String refreshToken) {
        SessionModel sessionModel = new SessionModel();
        sessionModel.setCreatedDate(LocalDate.now());
        sessionModel.setExpiryDate(LocalDate.now().plusDays(7));
        sessionModel.setIsDeleted(false);
        sessionModel.setPhoneNumber(phoneNumber);
        sessionModel.setRefreshToken(refreshToken);
        return sessionModel;
    }
}
