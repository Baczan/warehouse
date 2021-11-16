package com.example.resource.dto;

public class HighlightedPartDTO {
    private int id;
    private String partName;
    private String warehouseNumber;
    private String highlightedPartName;

    public HighlightedPartDTO() {
    }

    public HighlightedPartDTO(int id, String partName, String warehouseNumber, String highlightedPartName) {
        this.id = id;
        this.partName = partName;
        this.warehouseNumber = warehouseNumber;
        this.highlightedPartName = highlightedPartName;
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

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public String getHighlightedPartName() {
        return highlightedPartName;
    }

    public void setHighlightedPartName(String highlightedPartName) {
        this.highlightedPartName = highlightedPartName;
    }
}
