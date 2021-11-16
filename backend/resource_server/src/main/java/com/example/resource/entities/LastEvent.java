package com.example.resource.entities;

import javax.persistence.*;

@Entity
@Table(name = "last_event")
public class LastEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "event_id",nullable = false)
    private String eventId;

    public LastEvent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
