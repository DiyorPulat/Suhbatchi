package com.example.suhbatchi.dto.response;

import com.example.suhbatchi.entity.Company;
import lombok.Data;

@Data
public class CompanyInfoResponseDto {
    private Long id;
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

    public static CompanyInfoResponseDto of(Company company) {
        CompanyInfoResponseDto dto = new CompanyInfoResponseDto();
        dto.setId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        dto.setCompanyInnNumber(company.getCompanyInnNumber());
        dto.setAccountNumber(company.getAccountNumber());
        dto.setBusinessType(company.getBusinessType());
        dto.setPhoneNumber(company.getPhoneNumber());
        dto.setEmail(company.getEmail());
        dto.setStatusCompany(company.getStatusCompany());
        dto.setCreatedDate(company.getCreatedDate());
        dto.setBusinessStructure(company.getBusinessStructure());
        dto.setCompanyAddress(company.getCompanyAddress());
        dto.setDirectorName(company.getDirectorName());
        dto.setDirectorAddress(company.getDirectorAddress());
        dto.setDirectorPhoneNumber(company.getDirectorPhoneNumber());
        dto.setDirectorEmail(company.getDirectorEmail());
        return dto;
    }
}
