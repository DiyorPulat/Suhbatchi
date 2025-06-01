package com.example.suhbatchi.dto.response;

public class ClientInfoResponse {
    String clientId;

    public ClientInfoResponse(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}
