package com.example.suhbatchi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
public class PricingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long   timeOfUse;
    private Long   monthOfUse;
    private String planStatus;
    private String cost;

}
