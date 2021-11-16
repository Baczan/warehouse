package com.example.resource.elasticSearchEntities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "parts")
@Setting(settingPath = "search-analyzer.json")
public class PartElastic {

    @Id
    private int id;


    @Field(type = FieldType.Text, name = "partName")
    private String partName;

    @Field(type = FieldType.Text, name = "partNameNgram", analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_index")
    private String partNameNgram;

    @Field(type = FieldType.Text, name = "warehouseNumber")
    private String warehouseNumber;

    @Field(type = FieldType.Keyword, name = "partNameKeyword")
    private String partNameKeyword;

    public PartElastic() {
    }

    public PartElastic(int id, String partName, String warehouseNumber) {
        this.id = id;
        this.partName = partName;
        this.warehouseNumber = warehouseNumber;
        this.partNameKeyword =partName;
        this.partNameNgram=partName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public String getPartNameKeyword() {
        return partNameKeyword;
    }

    public void setPartNameKeyword(String partNameKeyword) {
        this.partNameKeyword = partNameKeyword;
    }

    public void setPartName(String partName) {
        this.partName = partName;
        this.partNameKeyword =partName;
        this.partNameNgram = partName;
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public String getPartNameNgram() {
        return partNameNgram;
    }

    public void setPartNameNgram(String partNameNgram) {
        this.partNameNgram = partNameNgram;
    }


}
