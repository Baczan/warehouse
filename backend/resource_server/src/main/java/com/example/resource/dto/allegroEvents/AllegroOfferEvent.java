package com.example.resource.dto.allegroEvents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllegroOfferEvent {

    private String id;
    private String type;
    private AllegroOffer offer;
    private Date occurredAt;

    public AllegroOfferEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AllegroOffer getOffer() {
        return offer;
    }

    public void setOffer(AllegroOffer offer) {
        this.offer = offer;
    }

    public Date getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Date occurredAt) {
        this.occurredAt = occurredAt;
    }

    @Override
    public String toString() {
        return "AllegroOfferEvent{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", offer=" + offer +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
