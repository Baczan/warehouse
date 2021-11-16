package com.example.resource.entities;

import javax.persistence.*;

@Entity
@Table(name = "label")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "part_name",nullable = false)
    private String partName;

    @Column(name = "car_name",nullable = false)
    private String carName;

    @Column(name = "warehouse_number",nullable = false)
    private String warehouseNumber;

    @Column(name = "warehouse_number_scanned",nullable = false)
    private String warehouseNumberScanned = "";



    public Label() {
    }

    public Label(String partName, String carName, String warehouseNumber) {
        this.partName = partName;
        this.carName = carName;
        this.warehouseNumber = warehouseNumber;
    }

    public Label(String partName, String carName, String warehouseNumber, String warehouseNumberScanned) {
        this.partName = partName;
        this.carName = carName;
        this.warehouseNumber = warehouseNumber;
        this.warehouseNumberScanned = warehouseNumberScanned;
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

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public String getWarehouseNumberScanned() {
        return warehouseNumberScanned;
    }

    public void setWarehouseNumberScanned(String warehouseNumberScanned) {
        this.warehouseNumberScanned = warehouseNumberScanned;
    }
}
