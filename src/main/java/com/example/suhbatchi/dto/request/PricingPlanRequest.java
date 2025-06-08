package com.example.suhbatchi.dto.request;

public record PricingPlanRequest(
        String name,
        String description,
        Long   timeOfUse,
        Long   monthOfUse,
        String planStatus,
        String cost
) {

}
