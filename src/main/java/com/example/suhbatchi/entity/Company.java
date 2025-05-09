package com.example.suhbatchi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String companyInnNumber;
    private String accountNumber;
    private String businessType;
    private String phoneNumber;
    private String email;
    private String websiteUrl;
    private String statusCompany;
    private String createdDate;
    private String businessStructure;
    private String companyAddress;
    private String directorName;
    private String directorAddress;
    private String directorPhoneNumber;
    private String directorEmail;
}
