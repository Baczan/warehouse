package com.example.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDTO {

    private Map<String,PartDTO> parts;
    private Map<String,IndexedPartDTO> indexedParts;

    public DataDTO() {
    }

    public Map<String, PartDTO> getParts() {
        return parts;
    }

    public void setParts(Map<String, PartDTO> parts) {
        this.parts = parts;
    }

    public Map<String, IndexedPartDTO> getIndexedParts() {
        return indexedParts;
    }

    public void setIndexedParts(Map<String, IndexedPartDTO> indexedParts) {
        this.indexedParts = indexedParts;
    }
}
