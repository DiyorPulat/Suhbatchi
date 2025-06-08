package com.example.suhbatchi.dto.response;

import lombok.Data;

@Data
public class PricingPlanReponse {
    private Long planId;
    private String name;
    private String description;
    private Long   timeOfUse;
    private Long   monthOfUse;
    private String planStatus;
    private String cost;
}
