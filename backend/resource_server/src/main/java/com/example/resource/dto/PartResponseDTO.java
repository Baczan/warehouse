package com.example.resource.dto;

import java.util.List;

public class PartResponseDTO {

    private long hits;
    private List<HighlightedPartDTO> parts;

    public PartResponseDTO() {
    }

    public PartResponseDTO(long hits, List<HighlightedPartDTO> parts) {
        this.hits = hits;
        this.parts = parts;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public List<HighlightedPartDTO> getParts() {
        return parts;
    }

    public void setParts(List<HighlightedPartDTO> parts) {
        this.parts = parts;
    }
}
