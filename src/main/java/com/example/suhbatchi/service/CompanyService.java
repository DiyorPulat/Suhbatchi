package com.example.suhbatchi.service;

import com.example.suhbatchi.caller.TaxCaller;
import com.example.suhbatchi.dto.request.CompanySaveRequestDto;
import com.example.suhbatchi.dto.response.CompanyInfoResponseDto;
import com.example.suhbatchi.dto.response.CompanyResponseDTO;
import com.example.suhbatchi.entity.Company;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.exception.UserNotFoundException;
import com.example.suhbatchi.mapper.CompanyMapper;
import com.example.suhbatchi.repostory.CompanyRepostory;
import com.example.suhbatchi.repostory.UserRepostory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CompanyService {
    private final CompanyRepostory companyRepostory;
    private final TaxCaller taxCaller;
    private final CompanyMapper companyMapper;
    private final UserRepostory userRepostory;

    public CompanyService(CompanyRepostory companyRepostory, TaxCaller taxCaller, CompanyMapper companyMapper, UserRepostory userRepostory) {
        this.companyRepostory = companyRepostory;
        this.taxCaller = taxCaller;
        this.companyMapper = companyMapper;
        this.userRepostory = userRepostory;
    }

    public void createCompany(CompanySaveRequestDto requestDto,String phoneNumber) {
        log.info("createCompany");
        CompanyResponseDTO responseDTO = taxCaller.getTaxInfo(requestDto.getCompanyInnNumber());
        Company company = companyMapper.toCompanyEntity(responseDTO, requestDto);
        Optional<User> user = userRepostory.findByPhoneNumber(phoneNumber);
        if (user.isEmpty()){
            throw new UserNotFoundException();
        }
        try {
            companyRepostory.save(company);
        } catch (DataAccessException e) {
            log.error("Error saving company:{}", e.getMessage());
        }
    }


    public CompanyInfoResponseDto getCompanyByInnNumber(String innNumber) {
        log.info("getCompanyByInnNumber");
        return companyRepostory.findCompanyByCompanyInnNumber(innNumber)
                .map(CompanyInfoResponseDto::of)
                .orElseGet(() -> {
                    CompanyResponseDTO responseDTO = taxCaller.getTaxInfo(innNumber);
                    return companyMapper.toCompanyInfoResponseDto(responseDTO);
                });
    }
}
