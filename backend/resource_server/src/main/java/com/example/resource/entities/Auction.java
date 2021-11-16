package com.example.resource.entities;

import com.example.resource.dto.AuctionDTO;
import com.example.resource.dto.ReturnDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "auction_number",nullable = false)
    private String auctionNumber;

    @Column(name = "auction_title",nullable = false)
    private String auctionTitle;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(name = "price",nullable = false)
    private float price;

    @Column(name = "damaged",nullable = false)
    private boolean damaged;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "return_id")
    private Return return_id;

    public Auction() {
    }

    public Auction(Return return_id, AuctionDTO auctionDTO) {
        this.return_id = return_id;
        this.auctionNumber = auctionDTO.getAuctionNumber();
        this.auctionTitle = auctionDTO.getAuctionTitle();
        this.quantity = auctionDTO.getQuantity();
        this.price = auctionDTO.getPrice();
        this.damaged = auctionDTO.isDamaged();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuctionNumber() {
        return auctionNumber;
    }

    public void setAuctionNumber(String auctionNumber) {
        this.auctionNumber = auctionNumber;
    }

    public String getAuctionTitle() {
        return auctionTitle;
    }

    public void setAuctionTitle(String auctionTitle) {
        this.auctionTitle = auctionTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public Return getReturn_id() {
        return return_id;
    }

    public void setReturn_id(Return return_id) {
        this.return_id = return_id;
    }
}
