package com.example.resource.dto.allegroEvents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllegroOfferEventsResponse {

    private List<AllegroOfferEvent> offerEvents;

    public AllegroOfferEventsResponse() {
    }

    public List<AllegroOfferEvent> getOfferEvents() {
        return offerEvents;
    }

    public void setOfferEvents(List<AllegroOfferEvent> offerEvents) {
        this.offerEvents = offerEvents;
    }

    @Override
    public String toString() {
        return "AllegroOfferEventsResponse{" +
                "offerEvents=" + offerEvents +
                '}';
    }
}
