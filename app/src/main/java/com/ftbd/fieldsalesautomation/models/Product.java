package com.ftbd.fieldsalesautomation.models;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int stock;
    @SerializedName("image_url")
    private String imageUrl;

    public Product(int id, String name, String category, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // এই মেথডটি ছিল না, তাই CartManager-এ এরর আসছিল
    public int getId() {
        return id;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }


    public void setId(int id) {
        this.id = id;
    }
}