package com.example.androidarielprojectapp.test;

import java.io.Serializable;
import java.util.UUID;

/**
 * class to store information about rentals.
 */
public class RegisterNewRentDataObject implements Serializable {

    private int tool;
    private String ownerID;
    private int price = 0;
    private double lat;
    private double longi;
    private String rentID;
    private String phone;
    private String imagePath;
    private String userID;
    private String userPhone;
    UUID uuid;
    //uuid.toString().replace("-", "");

    public RegisterNewRentDataObject() {
        tool=0;
        ownerID="";
        imagePath = null; // local storage of image
        price = 0;
        phone = null;
        lat = 0;
        longi = 0;
        UUID uuid = UUID.randomUUID();
        rentID = uuid.toString().replace("-", "");
    }


    public RegisterNewRentDataObject(int tool,String OwnerID, int price, double lat, double longi,
                                     String userPhone, String imagePath,String userID) {
        this.tool = tool;
        this.ownerID=OwnerID;
        this.price = price;
        this.lat = lat;
        this.longi = longi;
        this.phone = userPhone;
        this.imagePath = imagePath;
        UUID uuid = UUID.randomUUID();
        rentID = uuid.toString().replace("-", "");
        this.userID = userID;
        this.userPhone = userPhone;


    }

    public RegisterNewRentDataObject(int tool, int price, double lat, double longi,
                                     String userPhone, String imagePath, String userID) {
        this.tool = tool;
        this.price = price;
        this.lat = lat;
        this.longi = longi;
        this.phone = userPhone;
        this.imagePath = imagePath;
        UUID uuid = UUID.randomUUID();
        rentID = uuid.toString().replace("-", "");
        this.userPhone = userPhone;
        this.userID = userID;
    }

    public RegisterNewRentDataObject(int tool,String OwnerID, int price, double lat, double longi,
                                     String userPhone, String imagePath) {
        this.tool = tool;
        this.ownerID=OwnerID;
        this.price = price;
        this.lat = lat;
        this.longi = longi;
        this.phone = userPhone;
        this.imagePath = imagePath;
        UUID uuid = UUID.randomUUID();
        rentID = uuid.toString().replace("-", "");
        this.userPhone = userPhone;
    }

    public int getTool() {
        return tool;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    public String getToolAsString() {
        if (tool == 10) {
            return "Scooter";
        } else {
            return "Bicycle";
        }
    }

    public String getPhone() {
        return phone;
    }
    public String getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(String ownerID)
    {
        this.ownerID=ownerID;
    }

    public void setPhone(String notes) {
        this.phone = notes;
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

    public String getPriceAsString() {
        return Integer.toString(price);
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPhone() {
        return this.userPhone;
    }

    public void setUserPhone() {
        this.userPhone = userPhone;
    }
}