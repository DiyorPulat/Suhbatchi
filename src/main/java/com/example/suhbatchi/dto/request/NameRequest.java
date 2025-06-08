package com.example.suhbatchi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NameRequest(
        @JsonProperty("name")
        String name,
        @JsonProperty("lastname")
        String lastname
) {
}
