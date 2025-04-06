package com.example.suhbatchi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String innNumber;
    private String accountNumber;
    private String businessType;
    private String phoneNumber;
    private String email;
    private String websiteUrl;
    private String statusCompany;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
