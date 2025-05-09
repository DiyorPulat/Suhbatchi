package com.example.suhbatchi.mapper;

import com.example.suhbatchi.dto.companyDtos.CompanyResponseDTO;
import com.example.suhbatchi.dto.companyDtos.CompanySaveRequestDto;
import com.example.suhbatchi.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toCompanyEntity(CompanyResponseDTO responseDTO, CompanySaveRequestDto requestDTO) {
        Company company = new Company();
        company.setCompanyInnNumber(requestDTO.getCompanyInnNumber());
        company.setCompanyName(requestDTO.getCompanyName());
        company.setStatusCompany(responseDTO.getCompany().getVatStatus());
        company.setBusinessStructure(responseDTO.getCompany().getBusinessStructureDetail().getName_uz_latn());
        company.setCreatedDate(responseDTO.getCompany().getRegistrationDate());
        company.setPhoneNumber(requestDTO.getPhoneNumber());
        company.setWebsiteUrl(requestDTO.getWebsiteUrl());
        return company;
    }
}
