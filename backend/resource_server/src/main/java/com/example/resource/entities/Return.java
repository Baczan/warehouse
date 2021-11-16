package com.example.resource.entities;

import com.example.resource.dto.AuctionDTO;
import com.example.resource.dto.ReturnDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "return_table")
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "account_number",nullable = false)
    private String accountNumber;

    @Column(name = "annotation",nullable = false)
    private String annotation;

    @Column(name = "invoice",nullable = false)
    private boolean invoice;

    @Column(name = "form",nullable = false)
    private boolean form;

    @Column(name = "status",nullable = false)
    private int status;

    @Column(name = "sum",nullable = false)
    private float sum;


    @OneToMany(
            mappedBy = "return_id",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Auction> auctions;

    public Return() {
    }

    public Return(ReturnDTO returnDTO) {
        this.name=returnDTO.getName();
        this.accountNumber=returnDTO.getAccountNumber();
        this.annotation=returnDTO.getAnnotation();
        this.invoice=returnDTO.isInvoice();
        this.form=returnDTO.isForm();
        this.status=returnDTO.getStatus();
        this.sum=returnDTO.getSum();

        this.auctions = new ArrayList<>();

        returnDTO.getAuctions().forEach(auctionDTO -> {
            auctions.add(new Auction(this,auctionDTO));
        });

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<Auction> auctions) {
        this.auctions = auctions;
    }
}
