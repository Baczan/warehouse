package com.example.resource.dto;

import javax.persistence.Column;
import java.util.Date;

public class NewCarDTO {

    private String carName;
    private String brand;
    private String year;
    private String productionYears;
    private String engine;
    private String engineCode;
    private String transmission;
    private String transmissionCode;
    private String body;
    private String ac;
    private String paintCode;
    private String electricWindows;
    private String electricMirrors;
    private String mileage;
    private String abs;
    private String vin;
    private String annotation;
    private Date dateAdded;
    private int id;

    public NewCarDTO() {
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProductionYears() {
        return productionYears;
    }

    public void setProductionYears(String productionYears) {
        this.productionYears = productionYears;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getTransmissionCode() {
        return transmissionCode;
    }

    public void setTransmissionCode(String transmissionCode) {
        this.transmissionCode = transmissionCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public String getElectricWindows() {
        return electricWindows;
    }

    public void setElectricWindows(String electricWindows) {
        this.electricWindows = electricWindows;
    }

    public String getElectricMirrors() {
        return electricMirrors;
    }

    public void setElectricMirrors(String electricMirrors) {
        this.electricMirrors = electricMirrors;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NewCarDTO{" +
                "carName='" + carName + '\'' +
                ", brand='" + brand + '\'' +
                ", year='" + year + '\'' +
                ", productionYears='" + productionYears + '\'' +
                ", engine='" + engine + '\'' +
                ", engineCode='" + engineCode + '\'' +
                ", transmission='" + transmission + '\'' +
                ", transmissionCode='" + transmissionCode + '\'' +
                ", body='" + body + '\'' +
                ", ac='" + ac + '\'' +
                ", paintCode='" + paintCode + '\'' +
                ", electricWindows='" + electricWindows + '\'' +
                ", electricMirrors='" + electricMirrors + '\'' +
                ", mileage='" + mileage + '\'' +
                ", abs='" + abs + '\'' +
                ", vin='" + vin + '\'' +
                ", annotation='" + annotation + '\'' +
                ", dateAdded=" + dateAdded +
                ", id=" + id +
                '}';
    }
}
