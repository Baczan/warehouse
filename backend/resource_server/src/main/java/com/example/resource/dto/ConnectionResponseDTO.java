package com.example.resource.dto;

public class ConnectionResponseDTO {
    private String service;
    private String username;

    public ConnectionResponseDTO() {
    }

    public ConnectionResponseDTO(String service, String username) {
        this.service = service;
        this.username = username;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
