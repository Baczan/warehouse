package com.example.resource.dto;

public class PartDTO {

    private String part_name;
    private String warehouse_number;

    public PartDTO() {
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getWarehouse_number() {
        return warehouse_number;
    }

    public void setWarehouse_number(String warehouse_number) {
        this.warehouse_number = warehouse_number;
    }

    @Override
    public String toString() {
        return "PartDTO{" +
                "part_name='" + part_name + '\'' +
                ", warehouse_number='" + warehouse_number + '\'' +
                '}';
    }
}
