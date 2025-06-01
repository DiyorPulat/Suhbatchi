package com.example.suhbatchi.service;

import com.example.suhbatchi.caller.TaxCaller;
import com.example.suhbatchi.dto.companyDtos.CompanyInfoResponseDto;
import com.example.suhbatchi.dto.companyDtos.CompanyResponseDTO;
import com.example.suhbatchi.dto.companyDtos.CompanySaveRequestDto;
import com.example.suhbatchi.entity.Company;
import com.example.suhbatchi.mapper.CompanyMapper;
import com.example.suhbatchi.repostory.CompanyRepostory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CompanyService {
    private final CompanyRepostory companyRepostory;
    private final TaxCaller taxCaller;
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepostory companyRepostory, TaxCaller taxCaller, CompanyMapper companyMapper) {
        this.companyRepostory = companyRepostory;
        this.taxCaller = taxCaller;
        this.companyMapper = companyMapper;
    }

    public void createCompany(CompanySaveRequestDto requestDto) {
        log.info("createCompany");
        CompanyResponseDTO responseDTO = taxCaller.getTaxInfo(requestDto.getCompanyInnNumber());
        Company company = companyMapper.toCompanyEntity(responseDTO, requestDto);
        try {
            companyRepostory.save(company);
        } catch (DataAccessException e) {
            log.error("Error saving company:{}", e.getMessage());
        }
    }


    public CompanyResponseDTO getCompanyByInnNumber(String innNumber) {
        log.info("getCompanyByInnNumber");
        CompanyResponseDTO responseDTO = taxCaller.getTaxInfo(innNumber);
        return responseDTO;
    }
}
