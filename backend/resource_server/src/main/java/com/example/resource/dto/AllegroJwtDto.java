package com.example.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllegroJwtDto {

    private String user_name;

    public AllegroJwtDto() {
    }

    public AllegroJwtDto(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "AllegroJwtDto{" +
                "user_name='" + user_name + '\'' +
                '}';
    }
}
