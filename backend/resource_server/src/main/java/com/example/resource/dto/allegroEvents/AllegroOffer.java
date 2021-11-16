package com.example.resource.dto.allegroEvents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllegroOffer {

    private String id;

    public AllegroOffer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AllegroOffer{" +
                "id='" + id + '\'' +
                '}';
    }
}
