package com.example.resource.dto;

import com.example.resource.entities.Auction;

public class AuctionDTO {

    private String auctionNumber;
    private String auctionTitle;
    private int quantity;
    private float price;
    private boolean damaged;

    public AuctionDTO() {
    }

    public AuctionDTO(Auction auction){
        this.auctionNumber = auction.getAuctionNumber();
        this.auctionTitle = auction.getAuctionTitle();
        this.quantity = auction.getQuantity();
        this.price = auction.getPrice();
        this.damaged = auction.isDamaged();
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

    @Override
    public String toString() {
        return "AuctionDTO{" +
                "auctionNumber='" + auctionNumber + '\'' +
                ", auctionTitle='" + auctionTitle + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", damaged=" + damaged +
                '}';
    }
}
