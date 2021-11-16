package com.example.resource.dto;

import com.example.resource.entities.Auction;
import com.example.resource.entities.Return;

import java.util.ArrayList;
import java.util.List;

public class ReturnDTO {

    private String name;
    private String accountNumber;
    private String annotation;
    private boolean invoice;
    private boolean form;
    private int status;
    private float sum;
    private List<AuctionDTO> auctions;

    public ReturnDTO() {
    }

    public ReturnDTO(Return returnEntity){
        this.name=returnEntity.getName();
        this.accountNumber=returnEntity.getAccountNumber();
        this.annotation=returnEntity.getAnnotation();
        this.invoice=returnEntity.isInvoice();
        this.form=returnEntity.isForm();
        this.status=returnEntity.getStatus();
        this.sum=returnEntity.getSum();

        this.auctions = new ArrayList<>();

        returnEntity.getAuctions().forEach(auction -> {
            auctions.add(new AuctionDTO(auction));
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public void setInvoice(boolean invoice) {
        this.invoice = invoice;
    }

    public boolean isForm() {
        return form;
    }

    public void setForm(boolean form) {
        this.form = form;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public List<AuctionDTO> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<AuctionDTO> auctions) {
        this.auctions = auctions;
    }

    @Override
    public String toString() {
        return "ReturnDTO{" +
                "name='" + name + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", annotation='" + annotation + '\'' +
                ", invoice=" + invoice +
                ", form=" + form +
                ", status=" + status +
                ", sum=" + sum +
                ", auctions=" + auctions +
                '}';
    }
}
