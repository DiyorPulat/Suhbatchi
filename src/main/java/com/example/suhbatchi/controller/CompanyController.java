package com.example.suhbatchi.controller;

import com.example.suhbatchi.dto.request.CompanySaveRequestDto;
import com.example.suhbatchi.dto.response.CompanyInfoResponseDto;
import com.example.suhbatchi.dto.response.UserInfo4PermitResponse;
import com.example.suhbatchi.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @Operation(
            summary = "Auth token kerak emas",
            description = "Company malumotlarini inn orqali olish agar companiya avval ruyhatdan o'tgan bulsa id bilan keladi, Agar ruyhatdam utmagan bulsa id null bo'ladi",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli ma'lumotlar olindi",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserInfo4PermitResponse.class)
                                    )
                            )
                    )
            }
    )
    @GetMapping("/company-info/{INN}")
    public CompanyInfoResponseDto getInfo(@PathVariable String INN) {
        return companyService.getCompanyByInnNumber(INN);
    }

    @Operation(
            summary = "Auth token kerak emas",
            description = "Company malumotlar bazasida saqlash uchun api",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli ma'lumotlar saqlandi",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserInfo4PermitResponse.class)
                                    )
                            )
                    )
            }
    )
    @PostMapping("/save/company-info")
    public ResponseEntity<?>  saveCompanyInfo(@RequestBody CompanySaveRequestDto companySaveRequestDto) {
        companyService.createCompany(companySaveRequestDto);
        return ResponseEntity.ok().build();
    }
}
