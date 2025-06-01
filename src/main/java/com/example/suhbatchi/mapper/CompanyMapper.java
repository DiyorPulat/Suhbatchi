package com.example.suhbatchi.mapper;

import com.example.suhbatchi.dto.companyDtos.CompanyInfoResponseDto;
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
        company.setAccountNumber(requestDTO.getAccountNumber());
        company.setEmail(responseDTO.getCompanyContact().getEmail());
        company.setBusinessType(String.valueOf(responseDTO.getCompany().getBusinessType()));
        company.setDirectorAddress(responseDTO.getDirectorAddress().getRegion() + " " +
                  responseDTO.getDirectorAddress().getDistrict() + " "
                + responseDTO.getDirectorAddress().getStreetName());
        company.setDirectorEmail(responseDTO.getDirectorContact().getEmail());
        company.setDirectorPhoneNumber(responseDTO.getDirectorContact().getPhone());
        company.setDirectorName(responseDTO.getDirector().getFirstName() + " " + responseDTO.getDirector().getLastName());
        return company;
    }

    public CompanyInfoResponseDto toCompanyInfoResponseDto(CompanyResponseDTO responseDTO) {
        CompanyInfoResponseDto companyInfoResponseDto = new CompanyInfoResponseDto();
        companyInfoResponseDto.setCompanyName(companyInfoResponseDto.getCompanyName());
        companyInfoResponseDto.setCompanyInnNumber(responseDTO.getCompany().getTin());
        companyInfoResponseDto.setStatusCompany(responseDTO.getCompany().getVatStatus());
        companyInfoResponseDto.setBusinessType(responseDTO.getCompany().getBusinessStructureDetail().getName_uz_latn());
        companyInfoResponseDto.setCreatedDate(responseDTO.getCompany().getRegistrationDate());
        companyInfoResponseDto.setPhoneNumber(responseDTO.getDirectorContact().getPhone());
        companyInfoResponseDto.setEmail(responseDTO.getCompanyContact().getEmail());
        companyInfoResponseDto.setBusinessType(String.valueOf(responseDTO.getCompany().getBusinessType()));
        companyInfoResponseDto.setDirectorAddress(responseDTO.getDirectorAddress().getRegion() + " " +
                responseDTO.getDirectorAddress().getDistrict() + " "
                + responseDTO.getDirectorAddress().getStreetName());
        companyInfoResponseDto.setDirectorEmail(responseDTO.getDirectorContact().getEmail());
        companyInfoResponseDto.setDirectorPhoneNumber(responseDTO.getDirectorContact().getPhone());
        companyInfoResponseDto.setDirectorName(responseDTO.getDirector().getFirstName() + " " + responseDTO.getDirector().getLastName());
        return companyInfoResponseDto;
    }
}
