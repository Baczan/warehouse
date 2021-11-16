package com.example.resource.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "new_car")
public class NewCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "car_name",nullable = false)
    private String carName;

    @Column(name = "date_added",nullable = false)
    private Date dateAdded;

    @Column(name = "brand")
    private String brand;

    @Column(name = "year")
    private String year;

    @Column(name = "production_years")
    private String productionYears;

    @Column(name = "engine")
    private String engine;

    @Column(name = "engine_code")
    private String engineCode;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "transmission_code")
    private String transmissionCode;

    @Column(name = "body")
    private String body;

    @Column(name = "ac")
    private String ac;

    @Column(name = "paint_code")
    private String paintCode;

    @Column(name = "electric_windows")
    private String electricWindows;

    @Column(name = "electric_mirrors")
    private String electricMirrors;

    @Column(name = "mileage")
    private String mileage;

    @Column(name = "abs")
    private String abs;

    @Column(name = "vin")
    private String vin;

    @Column(name = "annotation",length = 4096)
    private String annotation;


    public NewCar() {
    }

    public NewCar(String carName, Date dateAdded) {
        this.carName = carName;
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
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

    @Override
    public String toString() {
        return "NewCar{" +
                "id=" + id +
                ", carName='" + carName + '\'' +
                ", dateAdded=" + dateAdded +
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
                '}';
    }
}
