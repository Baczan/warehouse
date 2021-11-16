package com.example.resource.entities;

import javax.persistence.*;

@Entity
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "part_name",nullable = false)
    private String partName;

    @Column(name = "warehouse_number",nullable = false)
    private String warehouseNumber;

    public Part() {
    }

    public Part(String partName, String warehouseNumber) {
        this.partName = partName;
        this.warehouseNumber = warehouseNumber;
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
}
