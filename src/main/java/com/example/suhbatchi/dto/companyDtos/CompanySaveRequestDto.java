package com.example.suhbatchi.dto.companyDtos;

import lombok.Data;

@Data
public class CompanySaveRequestDto {
    private String companyName;
    private String companyInnNumber;
    private String accountNumber;
    private String businessType;
    private String phoneNumber;
    private String email;
    private String websiteUrl;
    private String pricingPlan;
}
