package com.example.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllegroSellingModeDTO {

    private AllegroPriceDTO price;

    public AllegroSellingModeDTO() {
    }

    public AllegroPriceDTO getPrice() {
        return price;
    }

    public void setPrice(AllegroPriceDTO price) {
        this.price = price;
    }
}
