package com.example.resource.entities;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "car_id",nullable = false)
    private int carId;

    @Column(name = "extension",nullable = false)
    private String extension;

    @Column(name = "content_type",nullable = false)
    private String contentType;

    public Image() {
    }

    public Image(int carId, String extension) {
        this.carId = carId;
        this.extension = extension;
    }

    public Image(int carId, String extension, String contentType) {
        this.carId = carId;
        this.extension = extension;
        this.contentType = contentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", carId=" + carId +
                ", extension='" + extension + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
