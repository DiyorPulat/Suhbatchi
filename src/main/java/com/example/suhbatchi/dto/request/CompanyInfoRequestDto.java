package com.example.suhbatchi.dto.request;

import lombok.Data;

@Data
public class CompanyInfoRequestDto {
    String Inn;

    public CompanyInfoRequestDto(String inn) {
        Inn = inn;
    }
}
