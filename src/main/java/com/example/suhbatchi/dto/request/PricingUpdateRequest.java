package com.example.suhbatchi.dto.request;

public record PricingUpdateRequest(
        Long id,
        String name,
        String description,
        Long   timeOfUse,
        Long   monthOfUse,
        String planStatus,
        String cost
) {
}
