package com.example.androidarielprojectapp.test;

import java.util.UUID;

/**
 * simple class to store information about rentals.
 */
public class RegisterNewRentDataObject {

    int tool;
    int price = 0;
    double latitude;
    double longitude;
    String rentID;
    String notes;
    String imagePath;



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


    public RegisterNewRentDataObject(int tool, String notes, int price) {
        this.tool = tool;
        this.notes = notes;
        this.price = price;
        UUID uuid = UUID.randomUUID();
        rentID = uuid.toString();
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

    public String getrentID() {
        return rentID;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRentID(String rentID) {
        this.rentID = rentID;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getPrice() {
        return price;
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