package com.example.suhbatchi.dto.companyDtos;

import lombok.Data;

@Data
public class CompanyInfoResponseDto {
    private String companyName;
    private String companyInnNumber;
    private String accountNumber;
    private String businessType;
    private String phoneNumber;
    private String email;
    private String statusCompany;
    private String createdDate;
    private String businessStructure;
    private String companyAddress;
    private String directorName;
    private String directorAddress;
    private String directorPhoneNumber;
    private String directorEmail;
}
