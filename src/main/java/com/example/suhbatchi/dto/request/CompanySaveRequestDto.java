package com.example.suhbatchi.dto.request;

import lombok.Data;

@Data
public class CompanySaveRequestDto {
    private String companyInnNumber;
    private String accountNumber;
    private String phoneNumber;
    private String email;
    private String websiteUrl;
    private String pricingPlan;
}
