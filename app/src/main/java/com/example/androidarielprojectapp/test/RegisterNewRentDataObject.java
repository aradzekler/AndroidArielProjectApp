package com.example.androidarielprojectapp.test;

import java.util.UUID;

/**
 * class to store information about rentals.
 */
public class RegisterNewRentDataObject {

    private int tool;
    private int price = 0;
    private double latitude;
    private double longitude;
    private String rentID;
    private String notes;
    private String imagePath;


    public RegisterNewRentDataObject() {
        imagePath = null; // local storage of image
        price = 0;
        notes = null;
        tool = 0;
        latitude = 0;
        longitude = 0;
        UUID uuid = UUID.randomUUID();
        rentID = uuid.toString().replace("-", "");
    }


    public RegisterNewRentDataObject(int tool, int price, double latitude, double longitude,
                                     String notes, String imagePath) {
        this.tool = tool;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notes = notes;
        this.imagePath = imagePath;
        UUID uuid = UUID.randomUUID();
        rentID = uuid.toString().replace("-", "");


    }

    public int getTool() {
        return tool;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getrentID() {
        return rentID;
    }

    public void setRentID(String rentID) {
        this.rentID = rentID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public void setLongitude(double longi) {
        this.longitude = longi;
    }

    public Double getLat() {
        return latitude;
    }

    public Double getLongi() {
        return longitude;
    }
}