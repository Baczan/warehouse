package com.example.resource.elasticSearchEntities;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;
@Setting(settingPath = "search-analyzer.json")
public class AuctionElastic {

    @Field(type = FieldType.Text, name = "auctionNumber")
    private String auctionNumber;

    @Field(type = FieldType.Text, name = "auctionTitle")
    private String auctionTitle;

    @Field(type = FieldType.Text, name = "auctionNumberNgram", analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_index")
    private String auctionNumberNgram;

    @Field(type = FieldType.Text, name = "auctionTitleNgram", analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_index")
    private String auctionTitleNgram;

    public AuctionElastic() {
    }

    public AuctionElastic(String auctionName, String auctionTitle) {
        this.auctionNumber = auctionName;
        this.auctionNumberNgram = auctionName;
        this.auctionTitle = auctionTitle;
        this.auctionTitleNgram = auctionTitle;
    }

    public String getAuctionNumber() {
        return auctionNumber;
    }

    public void setAuctionNumber(String auctionName) {
        this.auctionNumber = auctionName;
        this.auctionNumberNgram = auctionName;
    }

    public String getAuctionTitle() {
        return auctionTitle;
    }

    public void setAuctionTitle(String auctionTitle) {
        this.auctionTitle = auctionTitle;
        this.auctionTitleNgram = auctionTitle;
    }

    public String getAuctionNumberNgram() {
        return auctionNumberNgram;
    }

    public void setAuctionNumberNgram(String auctionNameNgram) {
        this.auctionNumberNgram = auctionNameNgram;
    }

    public String getAuctionTitleNgram() {
        return auctionTitleNgram;
    }

    public void setAuctionTitleNgram(String auctionTitleNgram) {
        this.auctionTitleNgram = auctionTitleNgram;
    }
}
