package com.example.resource.elasticSearchEntities;

import com.example.resource.entities.Return;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "returns")
@Setting(settingPath = "search-analyzer.json")
public class ReturnElastic {

    @Id
    private int id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "nameNgram", analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_index")
    private String nameNgram;


    @Field(type = FieldType.Object, name = "auctions")
    private List<AuctionElastic> auctions;

    public ReturnElastic() {
    }

    public ReturnElastic(Return returnEntity) {
        this.id = returnEntity.getId();
        this.name = returnEntity.getName();
        this.nameNgram = returnEntity.getName();

        auctions = new ArrayList<>();

        returnEntity.getAuctions().forEach(auction -> {
            auctions.add(new AuctionElastic(auction.getAuctionNumber(),auction.getAuctionTitle()));
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
        this.nameNgram = name;
    }

    public String getNameNgram() {
        return nameNgram;
    }

    public void setNameNgram(String nameNgram) {
        this.nameNgram = nameNgram;
    }

    public List<AuctionElastic> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<AuctionElastic> auctions) {
        this.auctions = auctions;
    }


}
