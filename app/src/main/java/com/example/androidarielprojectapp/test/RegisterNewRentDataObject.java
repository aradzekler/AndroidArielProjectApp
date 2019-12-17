package com.example.androidarielprojectapp.test;

/**
 * simple class to store information
 */
public class RegisterNewRentDataObject {

    int tool;
    String notes;
    String price;
    String imagePath;

    public RegisterNewRentDataObject() {
        imagePath = null;
        price = null;
        notes = null;
        tool = 00;
    }


    public RegisterNewRentDataObject(int tool, String notes, String price) {
        this.tool = tool;
        this.notes = notes;
        this.price = price;
    }

    public int getTool() {
        return tool;
    }

    public void setTool(String recipeColor) {
        this.tool = tool;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public String getImagePath() {
        return imagePath;
    }


}