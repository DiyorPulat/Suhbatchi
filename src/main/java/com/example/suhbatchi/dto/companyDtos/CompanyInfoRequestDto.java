package com.example.suhbatchi.dto.companyDtos;

import lombok.Data;

@Data
public class CompanyInfoRequestDto {
    final String Inn;

    public CompanyInfoRequestDto(String inn) {
        Inn = inn;
    }
}
