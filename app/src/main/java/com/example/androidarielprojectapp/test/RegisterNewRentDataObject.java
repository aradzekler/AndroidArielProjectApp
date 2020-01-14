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
    private String renterID;
    private String notes;
    private String imagePath;
    private String userPhone;
    UUID uuid;
    //uuid.toString().replace("-", "");

    public RegisterNewRentDataObject() {
        tool=0;
        ownerID="";
        imagePath = null; // local storage of image
        price = 0;
        notes = null;
        lat = 0;
        longi = 0;
        uuid = UUID.randomUUID();
        renterID = "";
    }


    public RegisterNewRentDataObject(int tool,String OwnerID, int price, double lat, double longi,
                                     String userPhone, String imagePath) {
        this.tool = tool;
        this.ownerID=OwnerID;
        this.price = price;
        this.lat = lat;
        this.longi = longi;
        this.notes = notes;
        this.imagePath = imagePath;
        uuid = UUID.randomUUID();
        renterID = "";
        this.userPhone = userPhone;


    }

    public String getnotes() {
        return notes;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public int gettool() {return tool;}
    public void settool(int tool) {this.tool=tool;}

    public void setOwnerID() {
        this.ownerID= ownerID;
    }

    public void setnotes(String notes) {
        this.notes = notes;
    }

    public String getrenterID() { return renterID; }

    public void setrenterID(String rentID) {
        this.renterID = rentID;
    }

    public String getimagePath() {
        return imagePath;
    }

    public void setimagePath(String path) {
        this.imagePath = path;
    }

    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        this.price = price;
    }

    public void setlat(double lat) {
        this.lat = lat;
    }

    public void setlongi(double longi) { this.longi = longi; }

    public Double getLat() {
        return lat;
    }

    public Double getLongi() {
        return longi;
    }

    protected String getuuid(){return uuid.toString();}

    public String getuserPhone() {
        return this.userPhone;
    }

    public void setuserPhone() { this.userPhone = userPhone; }
}