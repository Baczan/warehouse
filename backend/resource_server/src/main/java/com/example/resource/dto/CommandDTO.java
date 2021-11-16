package com.example.resource.dto;

import java.util.ArrayList;
import java.util.List;

public class CommandDTO {

    private String warehouseNumber;

    private List<String> labelNumbers;

    public CommandDTO() {
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public List<String> getLabelNumbers() {
        return labelNumbers;
    }

    public void setLabelNumbers(List<String> labelNumbers) {
        this.labelNumbers = labelNumbers;
    }

    @Override
    public String toString() {
        return "CommandDTO{" +
                "warehouseNumber='" + warehouseNumber + '\'' +
                ", labelNumbers=" + labelNumbers +
                '}';
    }
}
