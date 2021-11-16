package com.example.resource.dto;

public class IndexedPartDTO {

    private String part_name;
    private String car_name;
    private String warehouse_number;
    private String date_added;
    private int index;

    public IndexedPartDTO() {
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getWarehouse_number() {
        return warehouse_number;
    }

    public void setWarehouse_number(String warehouse_number) {
        this.warehouse_number = warehouse_number;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "IndexedPartDTO{" +
                "part_name='" + part_name + '\'' +
                ", car_name='" + car_name + '\'' +
                ", warehouse_number='" + warehouse_number + '\'' +
                ", date_added='" + date_added + '\'' +
                ", index=" + index +
                '}';
    }
}
