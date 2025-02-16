package com.example.suhbatchi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NameRequest(
        @JsonProperty("name")
        String name,
        @JsonProperty("lastname")
        String lastname
) {
}
