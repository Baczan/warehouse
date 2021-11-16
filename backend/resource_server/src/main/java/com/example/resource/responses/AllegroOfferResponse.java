package com.example.resource.responses;

import com.example.resource.dto.AllegroOfferDto;

import java.util.ArrayList;
import java.util.List;

public class AllegroOfferResponse {
    private String id;
    private String name;
    private List<String> images;
    private float price;

    public AllegroOfferResponse(AllegroOfferDto offer) {
        id = offer.getId();
        name = offer.getName();
        price = Float.parseFloat(offer.getSellingMode().getPrice().getAmount());
        images = new ArrayList<>();
        offer.getImages().forEach(image->{
            images.add(image.getUrl());
        });
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
