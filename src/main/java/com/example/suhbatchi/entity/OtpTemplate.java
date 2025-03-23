package com.example.suhbatchi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class OtpTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otpCode;
    private String otpStatus;
    private String phoneNumber;
    private Integer messageId;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    private LocalDateTime createdAt;


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public void setOtpStatus(String otpStatus) {
        this.otpStatus = otpStatus;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Long getId() {
        return id;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public String getOtpStatus() {
        return otpStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getMessageId() {
        return messageId;
    }
}
