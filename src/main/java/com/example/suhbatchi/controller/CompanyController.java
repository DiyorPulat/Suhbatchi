package com.example.suhbatchi.controller;

import com.example.suhbatchi.caller.TaxCaller;
import com.example.suhbatchi.dto.companyDtos.CompanyInfoResponseDto;
import com.example.suhbatchi.dto.companyDtos.CompanyResponseDTO;
import com.example.suhbatchi.dto.companyDtos.CompanySaveRequestDto;
import com.example.suhbatchi.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {
    private final TaxCaller taxCaller;
    private final CompanyService companyService;

    public CompanyController(TaxCaller taxCaller, CompanyService companyService) {
        this.taxCaller = taxCaller;
        this.companyService = companyService;
    }

    @GetMapping("/company-info/{INN}")
    public CompanyResponseDTO getInfo(@PathVariable String INN) {
        return companyService.getCompanyByInnNumber(INN);
    }

    @PostMapping("/save/company-info")
    public ResponseEntity<?> saveCompanyInfo(@RequestBody CompanySaveRequestDto companySaveRequestDto) {
        companyService.createCompany(companySaveRequestDto);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
