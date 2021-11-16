package com.example.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllegroOfferDto {

    private String id;
    private String name;
    private List<AllegroImageDTO> images;
    private AllegroSellingModeDTO sellingMode;

    public AllegroOfferDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AllegroImageDTO> getImages() {
        return images;
    }

    public void setImages(List<AllegroImageDTO> images) {
        this.images = images;
    }

    public AllegroSellingModeDTO getSellingMode() {
        return sellingMode;
    }

    public void setSellingMode(AllegroSellingModeDTO sellingMode) {
        this.sellingMode = sellingMode;
    }
}
