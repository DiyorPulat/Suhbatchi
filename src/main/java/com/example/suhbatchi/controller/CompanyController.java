package com.example.suhbatchi.controller;

import com.example.suhbatchi.caller.TaxCaller;
import com.example.suhbatchi.dto.companyDtos.CompanyResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {
    private final TaxCaller taxCaller;

    public CompanyController(TaxCaller taxCaller) {
        this.taxCaller = taxCaller;
    }

    @GetMapping("/company-info/{INN}")
    public CompanyResponseDTO getInfo(@PathVariable String INN) {
        return taxCaller.getTaxInfo(INN);
    }

    @PostMapping("/save/company-info")
    public ResponseEntity<?> saveCompanyInfo() {
        return new ResponseEntity<>(HttpStatusCode.valueOf(122));
    }
}
